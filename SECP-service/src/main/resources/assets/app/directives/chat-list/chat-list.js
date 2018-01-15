'use strict';

angular.module('SECP')
    .directive('chatList', function () {
    return {
        restrict: 'E', //E = element, A = attribute, C = class, M = comment
        scope: {
            //@ reads the attribute value, = provides two-way binding, & works with functions
            contacts: '=',
            contactSelected: '&contactSelectedFn'
         },
        templateUrl: 'directives/chat-list/chat-list.html',
        link: function ($scope, element, attrs) {
            $(".chat-list-wrapper").niceScroll({autohidemode:'leave'});
            $scope.setActive = function(contact) {
                if($scope.activeContact !== contact) {
                    $scope.activeContact = contact;
                    $scope.contactSelected({'contact' : contact});
                }
            };

            $scope.getTime = function(time) {
                if(time) {
                    var date = new Date(time);
                    return moment(date).startOf('hour').fromNow();
                }
            }

           $scope.$watch('contacts', function(contacts) {
              if (contacts !== undefined && contacts.length > 0) {
                 $scope.setActive(contacts[0]);
              }
           });
        } //DOM manipulation
    }
});
