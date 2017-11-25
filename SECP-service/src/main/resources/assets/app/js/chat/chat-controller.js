'use strict';

angular.module('SECP')
  .controller('ChatController', ['$scope', '$modal', 'Chat',
    function ($scope, $modal, Chat) {

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

      $scope.create = function () {
        $scope.clear();
        $scope.open();
      };

      $scope.update = function (id) {
        $scope.chat = Chat.get({id: id});
        $scope.open(id);
      };

      $scope.delete = function (id) {
        Chat.delete({id: id},
          function () {
            $scope.chats = Chat.query();
          });
      };

      $scope.save = function (id) {
        if (id) {
          Chat.update({id: id}, $scope.chat,
            function () {
              $scope.chats = Chat.query();
              $scope.clear();
            });
        } else {
          Chat.save($scope.chat,
            function () {
              $scope.chats = Chat.query();
              $scope.clear();
            });
        }
      };

      $scope.clear = function () {
        $scope.chat = {

          "myattr": "",

          "id": ""
        };
      };

      $scope.open = function (id) {
        var chatSave = $modal.open({
          templateUrl: 'chat-save.html',
          controller: 'ChatSaveController',
          resolve: {
            chat: function () {
              return $scope.chat;
            }
          }
        });

        chatSave.result.then(function (entity) {
          $scope.chat = entity;
          $scope.save(id);
        });
      };
    }])
  .controller('ChatSaveController', ['$scope', '$modalInstance', 'chat',
    function ($scope, $modalInstance, chat) {
      $scope.chat = chat;



      $scope.ok = function () {
        $modalInstance.close($scope.chat);
      };

      $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
      };
    }]);
