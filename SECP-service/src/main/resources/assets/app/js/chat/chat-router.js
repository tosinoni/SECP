'use strict';

angular.module('SECP')
  .config(['$routeProvider', function ($routeProvider) {
    $routeProvider
      .when('/chats', {
        templateUrl: 'views/chat/chats.html',
        controller: 'ChatController'
      })
    }]);
