'use strict';

angular.module('SECP')
  .factory('Chat', function($http) {
    // factory returns an object
    // you can run some code before

    return {
        getMessages : function(user) {
            return $http.get("mockedResponses/messages.json")
            .then(function(res) {
                   //First function handles success
                   var userId = user.id;
                   return res.data[userId];
            }, function(err) {
                //Second function handles error
                console.log(err);
                console.log("error getting message list for user !!" + userId)
            });
        },

        getCurrentUser : function() {
           var user = {
              'id' : '1',
              'username': 'Kevin Mckoy'
           };

           return user;
         },

        getChatList : function() {
            return $http.get("mockedResponses/chatList.json")
            .then(function(res) {
                   //First function handles success
                   return res.data;
            }, function(err) {
                //Second function handles error
                console.log(err);
                console.log("error getting chat list!!")
            });
        }

    }
});
