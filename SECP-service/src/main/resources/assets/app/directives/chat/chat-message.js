'use strict';

angular.module('SECP')
    .directive('chatMessage', function () {
    return {
        restrict: 'E', //E = element, A = attribute, C = class, M = comment
        scope: {
            //@ reads the attribute value, = provides two-way binding, & works with functions
            messages: '=',
            currentUser: '='
         },
        templateUrl: 'directives/chat/chat-message.html',
        link: function ($scope, element, attrs) {

        //move the scroll button down to see the latest message
            $('#chat-scroll').animate({
                    scrollTop: $('#chat-scroll').get(0).scrollHeight
            });
        } //DOM manipulation
    }
});
