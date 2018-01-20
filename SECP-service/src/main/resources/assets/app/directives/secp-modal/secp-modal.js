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

            $scope.$watch('data', function(data) {
                $scope.modalData = angular.copy(data);
            });
            $scope.saveInput = function() {
                $scope.save({data:$scope.modalData});
            }
        } //DOM manipulation
    };
});
