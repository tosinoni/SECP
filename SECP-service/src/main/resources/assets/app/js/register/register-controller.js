angular.module('SECP')
    .controller('RegisterController',
        function ($scope, RegisterService, Auth, $location) {
        $scope.verifyUsernameUrl = "/SECP/user/verify/username/";
        $scope.verifyEmailUrl = "/SECP/user/verify/email/";

        $scope.register = function () {
            RegisterService.register($scope.user).then(function(res) {
                if(res.status == 201) {
                    swal('Registration successful!', null, 'success');
                    $scope.frm.$setPristine();
                    $scope.frm.$setUntouched();
                    $scope.user = {};
                    $scope.password_conf = null;
                } else {
                    swal('Oops..!', res.data.message,'error');
                }
            });
        }
    });
