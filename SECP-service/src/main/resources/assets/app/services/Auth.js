'use strict';

angular.module('SECP')
  .factory('Auth', function($http, jwtHelper) {

    var user;

    var config = {skipAuthorization: true};


    return {
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
        }
    }
  });
