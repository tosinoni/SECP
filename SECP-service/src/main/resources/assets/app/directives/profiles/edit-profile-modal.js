'use strict';

angular.module('SECP')
    .directive('editProfileModal', function (Admin) {
    return {
        restrict: 'E',
        scope: {
            name: "@",
            imgurl: "@"
         },
        templateUrl: 'directives/profiles/edit-profile-modal.html',
        link: function ($scope, element, attrs) {
        }
    };
});
