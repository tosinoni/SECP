'use strict';

angular.module('SECP')
  .factory('Chat', function($http) {
    // factory returns an object
    // you can run some code before

    return {
        getMessages : function(group) {
            return $http.get("/SECP/messages/group/" + group.groupID)
            .then(function(res) {
                //First function handles success
                if(res.status == 200) {
                    return res.data;
                }
            }, function(err) {
                //Second function handles error
                console.log(err);
                console.log("error getting message list for user !!" + userId)
            });
        },

        getCurrentUser : function() {
           return $http.get("/SECP/user/profile")
           .then(function(res) {
               if(res.status == 200) {
                   return res.data;
               }
           }, function(err) {
               //Second function handles error
               console.log(err);
               console.log("error getting !!" + userId)
           });
         },

        getChatList : function() {
            return $http.get("/SECP/groups/user")
            .then(function(res) {
                //First function handles success
                if (res.status == 200) {
                    return res.data;
                }
            }, function(err) {
                //Second function handles error
                console.log(err);
                console.log("error getting chat list!!")
            });
        }

    }
});
