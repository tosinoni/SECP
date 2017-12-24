'use strict';

angular.module('SECP')
  .factory('Auth', function($http, jwtHelper) {

    var user;
    var config = {skipAuthorization: true};


    return {
        ADMIN: 'ADMIN',
        login : function(user) {
            return $http.post("/SECP/login", user, config)
            .then(function(res) {
                 var user = res.data;
                 localStorage.setItem('token', user.token);
                 localStorage.setItem('user', user.userID);
                 localStorage.setItem('loginRole', user.loginRole);
                 localStorage.setItem('username', user.username);
                 return res;
            }, function(err) {
                return err;
            });
        },

        isTokenExpired : function(){
            var token = localStorage.getItem('token');
            if(token) {
                return  jwtHelper.isTokenExpired(token);
            }
            return false;
        },

        logout : function(){
            localStorage.clear();
        },

        isUserAnAdmin: function() {
            var userID = localStorage.getItem('user');

            return $http.get("/SECP/user/verify/admin/" + userID)
            .then(function(res) {
                if (res.status == 200) {
                    return true;
                }
                return false
            }, function(err) {
               return false;
            });
        }
    }
  });
