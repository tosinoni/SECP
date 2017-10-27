'use strict';

angular.module('SECP')
  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider
      .when('/chats', {
        templateUrl: 'views/chat/chats.html',
        controller: 'ChatController',
        resolve:{
          resolvedChat: ['Chat', function (Chat) {
            return Chat.query();
          }]
        }
      })
    }]);
