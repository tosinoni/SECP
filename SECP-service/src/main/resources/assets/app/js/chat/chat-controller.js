'use strict';

angular.module('SECP')
  .controller('ChatController',
    function ($scope, $modal, Chat, Socket) {
      //declaring variables
      $scope.contacts = [];

      Chat.getCurrentUser().then(function(user) {
        if(user) {
            $scope.currentUser = user;
        }
      });
      $scope.clicked = false;
      console.log(new Date());
      Socket.onmessage(function (message) {
        var messageObj = JSON.parse(message);
        $scope.messages.push(messageObj);
        toastr.success(messageObj.body, messageObj.senderId);
      });

      Socket.onopen(function () {
        $scope.websocketConnected = true;
      });

      Chat.getChatList().then(function(data) {
        console.log(data);
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
            timestamp: new Date()
         };

         Socket.send(messageDTO);
         $scope.messages.push(messageDTO)
         //clearing the message input in the textarea
         $scope.messageInput = null;
      };

        $scope.sendMessageUsingEnter = function() {
            var code = event.keyCode || event.which;
            if (code === 13) {
                if (!event.shiftKey) {
                    event.preventDefault();
                    $scope.sendMessage();
                }
            }
        };

      $scope.contactSelected = function(contact) {
         $scope.selectedChat = contact;
         Chat.getMessages(contact).then(function(data) {
            if(data) {
                $scope.messages = data.reverse();
            }
         });
      };
  });
