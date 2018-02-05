'use strict';

angular.module('SECP')
    .factory('UserService', function($http) {
        return {
            getUser: function (id) {
                return $http.get("/SECP/user/id/" + id)
                    .then(function (res) {
                        if (res.status == 200) {
                            return res.data;
                        }
                    }, function (err) {
                        return err;
                    });
            }
        }
    });
