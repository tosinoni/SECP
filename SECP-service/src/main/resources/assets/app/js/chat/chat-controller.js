'use strict';

angular.module('SECP')
  .controller('ChatController',
    function ($scope, $modal, Chat, Socket) {


      //declaring variables
      $scope.contacts = [];
      $scope.currentUser = Chat.getCurrentUser();
      $scope.clicked = false;

      Socket.onmessage(function (message) {
        var messageObj = JSON.parse(message);
        console.log(messageObj);
      });

      Socket.onopen(function () {
        console.log("websocket connected");
        $scope.websocketConnected = true;
      });

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
