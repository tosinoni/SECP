'use strict';

angular.module('SECP')
  .controller('ChatController',
    function ($scope, Chat, Socket, EncryptionService) {
      //declaring variables
      $scope.contacts = [];
      $scope.secretKeysForChat = {};
      $scope.searching = false;

      Chat.getCurrentUser().then(function(user) {
        if(user) {
            $scope.currentUser = user;
        }
      });

      EncryptionService.getDecryptedSecretKeys().then(function (userSecretKeys) {
          if(userSecretKeys) {
              $scope.secretKeysForChat = userSecretKeys;
          }

          //need the secret keys first
          Chat.getChatList().then(function(data) {
              if(data) {
                  $scope.contacts = data;
              }
          });
      });

      $scope.clicked = false;
      Socket.onmessage(function (message) {
        var messageObj = JSON.parse(message);
        console.log(messageObj);
        console.log("received message: " + messageObj.body);
        $scope.messages.push(messageObj);

        let decryptedMessage = EncryptionService.decryptMessage(messageObj.body, $scope.secretKeysForChat[messageObj.groupId]);
        toastr.success(decryptedMessage, messageObj.senderDisplayName);
        setLastMessageForContacts(messageObj.groupId, messageObj);
      });

      Socket.onopen(function () {
        $scope.websocketConnected = true;
      });

      var setLastMessageForContacts = function (groupID, message) {
        var index = _.findIndex($scope.contacts, function(o) { return o.groupID == groupID; });
        $scope.contacts[index].lastMessage = message;
      }

      $scope.sendMessage = function() {
          if ($scope.secretKeysForChat) {
              let groupID = $scope.selectedChat.groupID;
              let messageBody = EncryptionService.enryptMessage($scope.messageInput, $scope.secretKeysForChat[groupID]);

              console.log("sending message: " + messageBody);
              var messageDTO = {
                  groupId: groupID,
                  senderId: $scope.currentUser.userID,
                  body: messageBody,
                  reason: "message",
                  timestamp: moment()
              };

              Socket.send(messageDTO);
              $scope.messages.push(messageDTO)
              setLastMessageForContacts(messageDTO.groupId, messageDTO);
              //clearing the message input in the textarea
              $scope.messageInput = null;
          } else {
              swal("Oops..", "You cannot chat because you dont have the encryption keys", "error");
          }
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
            EncryptionService.sendSecretKeysToGroup(contact.groupID);
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
