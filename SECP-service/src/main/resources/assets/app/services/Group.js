'use strict';

angular.module('SECP')
  .factory('Group', function($http) {


    return {
        getProfile: function(groupID) {
            return $http.get("/SECP/groups/id/" + groupID)
            .then(function(res) {
                if (res.status == 200) {
                    return res.data;
                }
            }, function(err) {
               return false;
            });
        }
    }
  });
