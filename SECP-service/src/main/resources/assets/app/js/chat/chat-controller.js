'use strict';

angular.module('SECP')
  .controller('ChatController', ['$scope', '$modal', 'resolvedChat', 'Chat',
    function ($scope, $modal, resolvedChat, Chat) {

      $scope.chats = resolvedChat;

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
