'use strict';

angular.module('SECP')
    .directive('groupProfile', function () {
    return {
        restrict: 'E',
        scope: {
            id: '=',
            modalname: '@'
         },
        templateUrl: 'directives/profiles/group-profile.html',
        link: function ($scope, element, attrs) {
            $("#profileBlock").niceScroll({autohidemode:'leave'});
        } //DOM manipulation
    };
});
