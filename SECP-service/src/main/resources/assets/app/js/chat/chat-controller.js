'use strict';

angular.module('SECP')
  .controller('ChatController',
    function ($scope, $modal, Chat, Socket, Admin, $q) {
      //declaring variables
      $scope.contacts = [];
      $scope.searching = false;

      Chat.getCurrentUser().then(function(user) {
        if(user) {
            $scope.currentUser = user;
        }
      });

      $scope.clicked = false;
      Socket.onmessage(function (message) {
        var messageObj = JSON.parse(message);
        $scope.messages.push(messageObj);
        toastr.success(messageObj.body, messageObj.senderId);
        setLastMessageForContacts(messageObj.groupId, messageObj);
      });

      Socket.onopen(function () {
        $scope.websocketConnected = true;
      });

      var setLastMessageForContacts = function (groupID, message) {
        var index = _.findIndex($scope.contacts, function(o) { return o.groupID == groupID; });
        $scope.contacts[index].lastMessage = message;
      }

      Chat.getChatList().then(function(data) {
        if(data) {
            $scope.contacts = data;
        }
      });

      $scope.sendMessage = function(){
          var promise = $scope.messageContainsFilterWord();
          promise.then(function(messageContainsFilterWord) {
              if(!messageContainsFilterWord){
                  var messageDTO = {
                      groupId: $scope.selectedChat.groupID,
                      senderId: $scope.currentUser.userID,
                      body: $scope.messageInput,
                      reason: "message",
                      timestamp: moment()
                  };

                  Socket.send(messageDTO);
                  $scope.messages.push(messageDTO)
                  setLastMessageForContacts(messageDTO.groupId, messageDTO);
                  //clearing the message input in the textarea
                  $scope.messageInput = null;
              }
          });
        };

      $scope.messageContainsFilterWord = function() {
          var deffered = $q.defer();
          let senderPermission = $scope.currentUser.permission
          let senderRoles = $scope.currentUser.roles;
          let receiverPermissions = $scope.selectedChat.permissions;
          let receiverRoles = $scope.selectedChat.roles;
          //Split message input on space or comma
          let messageWords = $scope.messageInput.split(/[ ,]+/);
          Admin.getAllFilters().then( function (filterWordsSet) {
              if(filterWordsSet) {
                  var filterWords = {};
                  //Create map of filter words and names
                  for(let filterWord of filterWordsSet){
                      filterWords[filterWord.name]=filterWord;
                  }
                  //let filterWords = filterWordsSet.map(filterWord => filterWord.name);
                  for (let messageWord of messageWords) {
                      if (filterWords[messageWord]) {
                          if (!$scope.messageSendable(filterWords[messageWord].permissions, filterWords[messageWord].roles, senderPermission, senderRoles, receiverPermissions, receiverRoles)) {
                              swal(
                                  'Oops...',
                                  'Could not deliver message due to sensitive content!',
                                  'error'
                              )
                              deffered.resolve(true);
                          }
                      }
                  }
              }

              deffered.resolve(false);
          });
          return deffered.promise;
      };

      $scope.messageSendable = function(filterPermissions, filterRoles, senderPermission, senderRoles, receiverPermissions, receiverRoles){
          //Check to see if the sender permission is part of the allowed filter permissions
          if(filterPermissions.indexOf(senderPermission)>-1){
              //Perform the intersection of senderRoles and filterRoles
              if(senderRoles.filter((role) => filterRoles.includes(role))){
                  //Check to see if receiverPermissions is subset of filterPermissions
                  if(receiverPermissions.some((permission) => filterPermissions.indexOf(permission)>-1)){
                      //Check to see if receiverRoles is subset of filterRoles
                      if(receiverRoles.some((permission) => filterRoles.indexOf(permission)>-1)){
                          return true;
                      }
                  }
              }
          }
          return false;
      };

        $scope.sendMessageUsingEnter = function(event) {
            var code = event.keyCode || event.which;
            if (code === 13) {
                if (!event.shiftKey) {
                    event.preventDefault();
                    $scope.sendMessage();
                }
            }
        };

        var delayTimer;
        $scope.search = function(){
            if ($scope.searchInput) {
                var searchString = $scope.searchInput;
                clearTimeout(delayTimer);
                delayTimer = setTimeout(function() {
                   Chat.search(searchString).then(function(data) {
                       if(data) {
                           console.log(data);
                           $scope.result = data;
                       }
                   });
                }, 500);
                $scope.searching=true;
            } else {
                $scope.searching = false;
            }
        }

      $scope.contactSelected = function(contact) {
         var index = _.findIndex($scope.contacts, function(o) { return o.groupID == contact.groupID; });
         if(index < 0) {
            $scope.contacts.push(contact);
         }

         $scope.selectedChat = contact;
         Chat.getMessages(contact).then(function(data) {
            if(data) {
                $scope.messages = data.reverse();
            }
         });
      };

        $scope.$on('closeSearchResults', function (event, args) {
            $scope.searching = args.searching;
            //clearing the message input in the textarea
            $scope.searchInput = null;

        });
  });
