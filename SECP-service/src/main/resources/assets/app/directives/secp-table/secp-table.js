'use strict';

angular.module('SECP')
    .directive('secpTable', function () {
    return {
        restrict: 'E',
        scope: {
            name: '@',
            modalname: '@',
            buttonname: '@',
            data: '=',
            headers: '=',
            delete: '&deleteFn'
         },
        templateUrl: 'directives/secp-table/secp-table.html',
        link: function ($scope, element, attrs) {
            $scope.change = function(row) {
                $scope.delete({row: row});
            }
        } //DOM manipulation
    };
});
