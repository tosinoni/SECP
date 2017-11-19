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
            $scope.setActive = function(contact) {
                if($scope.activeContact !== contact) {
                    $scope.activeContact = contact;
                    $scope.contactSelected({'contact' : contact});
                }
            };

           $scope.$watch('contacts', function(contacts) {
              if (contacts !== undefined && contacts.length > 0) {
                 $scope.setActive(contacts[0]);
              }
           });
        } //DOM manipulation
    }
});
