'use strict';

angular.module('SECP')
    .directive('secpModal', function () {
    return {
        restrict: 'E',
        scope: {
            frmName: '@',
            title: '@',
            body: '@',
            name: '@',
            ngModel: '=',
            save: '&saveFn'
         },
        templateUrl: 'directives/secp-modal/secp-modal.html',
        link: function ($scope, element, attrs) {
            $scope.saveInput = function() {
                $scope.save();
            }
        } //DOM manipulation
    };
});
