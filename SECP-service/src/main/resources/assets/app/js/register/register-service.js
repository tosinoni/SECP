'use strict';

angular.module('SECP')
  .factory('RegisterService', function($http) {

    var config = {skipAuthorization: true};
    return {
        register : function(user) {
            return $http.post("/SECP/register", user, config)
            .then(function(res) {
                   return res;
            }, function(err) {
                return err;
            });
        }
    }
  });
