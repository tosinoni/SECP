angular.module('SECP')
    .controller('RegisterController', ['$scope', 'RegisterService',
        function ($scope, RegisterService) {
        $scope.verifyUsernameUrl = "/SECP/verify?username=";
        $scope.verifyEmailUrl = "/SECP/verify?email=";

        $scope.register = function () {
            RegisterService.register($scope.user).then(function(res) {
                if(res.status == 201) {
                    swal('Welcome to SECP!', 'Registration successful!','success');
                    $scope.frm.$setPristine();
                    $scope.frm.$setUntouched();
                    $scope.user = {};
                    $scope.password_conf = null;
                } else {
                    swal('Oops..!', res.data.message,'error');
                }

            });
        }
    }]);
