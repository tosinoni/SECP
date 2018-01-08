'use strict';

angular.module('SECP')
  .controller('ChatController',
    function ($scope, $modal, Chat, Socket) {
      Socket.onopen(function () {
        var message = {
            groupId: 24,
            reason: 'message',
            body: 'hi I am user' + localStorage.getItem('user')
        }
        Socket.send(message);
        $scope.websocketConnected = true;
      });

      Socket.onerror(function (error) {
        $scope.websocketConnected = false;
        swal('Oops', "The chat service is not available right now", "error");
      });

      Socket.onmessage(function (message) {
        console.log(message);
      });


      //declaring variables
      $scope.contacts = [];
      $scope.currentUser = Chat.getCurrentUser();


      Chat.getChatList().then(function(data) {
        $scope.contacts = data;
      });

      $scope.sendMessage = function() {
         var message = {
            'text': $scope.messageInput
         };
         $scope.messages.slice(-1)[0].contents.push(message)
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
            $scope.messages = data;
         });
      };
    });
