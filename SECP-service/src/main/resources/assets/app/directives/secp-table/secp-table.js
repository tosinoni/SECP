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
            delete: '&deleteFn',
            edit: '&editFn'
         },
        templateUrl: 'directives/secp-table/secp-table.html',
        link: function ($scope, element, attrs) {
            if(attrs.edit){
                $scope.isEditable=true;
            }
            $scope.change = function(row) {
                $scope.delete({row: row});
            }
            $scope.modify = function(row) {
                $scope.edit({row: row});
            }
        } //DOM manipulation
    };
});
