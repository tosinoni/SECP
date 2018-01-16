'use strict';

angular.module('SECP')
    .directive('secpManageModal', function (Admin) {
    return {
        restrict: 'E',
        scope: {
            frmname: '@',
            title: '@',
            inputname: '@',
            name: '@',
            modalData: '=', //this is the data to be used to populate the modal
            isuser: '@',
            isfilter: '@',
            save: '&saveFn'
         },
        templateUrl: 'directives/secp-manage-modal/secp-manage-modal.html',
        link: function ($scope, element, attrs) {
            //getting the adminRoles
            Admin.getRoles().then(function(res) {
                if (res) {
                    $scope.roles = res;
                }
            })

            //getting the permissions
            Admin.getPermissions().then(function(res) {
                if (res) {
                    $scope.permissions = res;
                }
            })

            $scope.submit = function() {
                $scope.save({row: $scope.modalData});
            }
        }
    };
});
