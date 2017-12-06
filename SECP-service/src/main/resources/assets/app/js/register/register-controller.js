angular.module('SECP')
    .controller('RegisterController',
        function ($scope, RegisterService, Auth, $location) {
        $scope.verifyUsernameUrl = "/SECP/verify?username=";
        $scope.verifyEmailUrl = "/SECP/verify?email=";

        $scope.register = function () {
            RegisterService.register($scope.user).then(function(res) {
                if(res.status == 201) {
                    var reqBody = {
                        username: $scope.user.userName,
                        password: $scope.user.password
                    };

                    Auth.login(reqBody).then(function(res) {
                        if(res.status == 200) {
                            swal('Welcome to SECP!', 'Registration successful!','success');
                            $location.path('/chats');
                        } else {
                            swal('Oops..!', res.data.message,'error');
                        }
                    });
                } else {
                    swal('Oops..!', res.data.message,'error');
                }
            });
        }
    });
