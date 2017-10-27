'use strict';

angular.module('SECP')
  .factory('Chat', ['$resource', function ($resource) {
    return $resource('SECP/chats/:id', {}, {
      'query': { method: 'GET', isArray: true},
      'get': { method: 'GET'},
      'update': { method: 'PUT'}
    });
  }]);
