'use strict';

angular.module('SECP')
    .directive('chatMessage', function ($http) {
    return {
        restrict: 'E', //E = element, A = attribute, C = class, M = comment
        scope: {
            //@ reads the attribute value, = provides two-way binding, & works with functions
            messages: '=',
            currentUser: '=',
            selectedChat: '=',
            clicked: '='
         },
        templateUrl: 'directives/chat-message/chat-message.html',
        link: function ($scope, element, attrs) {
            $(".message-list-wrapper").niceScroll({autohidemode:'leave'});
            $scope.$watch('messages', function(messages, oldmessages) {
                //move the scroll button down to see the latest message
                $('#chat-scroll').animate({
                   scrollTop: $('#chat-scroll').get(0).scrollHeight
                });
             }, true);

             $scope.$watch('selectedChat', function(selectedChat) {
                  if(selectedChat && selectedChat.users) {
                     var obj = {};
                     for(var user of selectedChat.users) {
                         obj[user.userID] = user;
                     }

                     $scope.users = obj;
                  }
             });

             $scope.getTime = function(time) {
                 if(time) {
                     var date = new Date(time);
                     return moment(date).startOf('hour').fromNow();
                 }
             }

             $scope.getMessageSender = function(senderId) {
                  if(senderId && $scope.users) {
                      return $scope.users[senderId];
                  }
              }

              $scope.getSenderAvatar = function(senderId) {
                 var sender = $scope.getMessageSender(senderId);
                 return sender.avatarUrl;
              }

              $scope.getSenderDisplayName = function(senderId) {
                   var sender = $scope.getMessageSender(senderId);
                   return sender.displayName;
                }

             $scope.clickProfile = function(){

                // NEED TO ADD CHECK TO SEE IF USER OR PROFILE, THEN GENERATE USER OR PROFILE DIV.
                $scope.clicked = !$scope.clicked;
                var chatBlock = document.getElementById("chatBlock");
                var profileBlock = $("#profileBlock");
                if($scope.clicked === false){
                    chatBlock.setAttribute("class", "col-md-8");
                    profileBlock.hide();
                }
                else{
                    chatBlock.setAttribute("class", "col-md-4");
                    profileBlock.fadeIn("slow");
                    profileBlock.show();
                }
             };
        } //DOM manipulation
    }
});
