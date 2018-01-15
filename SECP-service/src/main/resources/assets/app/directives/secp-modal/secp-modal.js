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
            save: '&saveFn'
         },
        templateUrl: 'directives/secp-modal/secp-modal.html',
        link: function ($scope, element, attrs) {
            $scope.saveInput = function() {
                $scope.save({data:$scope.data});
            }
        } //DOM manipulation
    };
});
