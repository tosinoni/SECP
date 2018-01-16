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
            data: '=',
            save: '&saveFn',
            disableInput: '='
         },
        templateUrl: 'directives/secp-modal/secp-modal.html',
        link: function ($scope, element, attrs) {
            $scope.saveInput = function() {
                console.log($scope.data);
                $scope.save({data:$scope.data});
            }
        } //DOM manipulation
    };
});
