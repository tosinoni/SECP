'use strict';

angular.module('SECP')
    .directive('secpRegisterModal', function (Admin) {
    return {
        restrict: 'E',
        scope: {
            save: '&saveFn'
         },
        templateUrl: 'directives/secp-register-modal/secp-register-modal.html',
        link: function ($scope, element, attrs) {
            $scope.verifyUsernameUrl = "/SECP/user/verify/username/";
            $scope.verifyEmailUrl = "/SECP/user/verify/email/";
            $('#registerModal').on('shown.bs.modal', function(){
                $('.modal-body').niceScroll();
            })
            $scope.registerUser = function() {
                $scope.save({user:$scope.user});
                $scope.frm.$setPristine();
                $scope.frm.$setUntouched();
                $scope.user = {};
                $scope.password_conf = null;
            }
            //getting the permissions
            Admin.getPermissions().then(function(res) {
                if (res) {
                    $scope.permissions = res;
                }
            });
        } //DOM manipulation
    };
});
