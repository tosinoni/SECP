'use strict';

angular.module('SECP')
  .controller('ChatController',
    function ($scope, Chat, Socket, EncryptionService) {
      //declaring variables
      $scope.contacts = [];
      $scope.secretKeysObj = [];
      $scope.searching = false;

      Chat.getCurrentUser().then(function(user) {
        if(user) {
            $scope.currentUser = user;
        }
      });

      EncryptionService.getDecryptedSecretKeys().then(function (keys) {
          if(keys) {
              console.log(keys);
              $scope.secretKeysObj = keys;
          }
      })

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

      $scope.sendMessage = function() {
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
