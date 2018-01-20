'use strict';

angular.module('SECP')
    .directive('searchResults', function (Group) {
    return {
        restrict: 'E', //E = element, A = attribute, C = class, M = comment
        scope: {
            //@ reads the attribute value, = provides two-way binding, & works with functions
            result: '=',
            contactSelected: '&contactSelectedFn'
         },
        templateUrl: 'directives/search-results/search-results.html',
        link: function ($scope, element, attrs) {
            $(".chat-list-wrapper").niceScroll({autohidemode:'leave'});

            $scope.setActive = function(contact) {
                if (contact.userID) {
                    Group.getPrivateGroup(contact).then(function(res) {
                        if (res) {
                             $scope.activeContact = res;
                             $scope.contactSelected({'contact' : res});
                        }
                    });
                }else if($scope.activeContact !== contact) {
                    $scope.activeContact = contact;
                    $scope.contactSelected({'contact' : contact});
                }
            };
            $scope.closeSearchResults = function() {
                $scope.$emit('closeSearchResults', {searching: false});
            }
        } //DOM manipulation
    }
});
