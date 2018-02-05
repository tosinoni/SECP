angular.module('SECP')
    .controller('LoginController', function ($scope, Auth, $location) {
        $scope.login = function () {
            // TODO: NEED TO SEND AUTHENTICATION EMAIL (IF REQUIRED BY ADMIN) CONTAINING VERIFICATION CODE WHEN SUCCESSFULLY LOGGED IN
            Auth.login($scope.user).then(function (res) {
                if (res.status == 200) {
                    swal('Welcome to SECP!', 'Login successful!', 'success');
                    $scope.registerDevice();
                } else {
                    swal('Oops..!', res.data.message, 'error');
                }
            });
        }

        $scope.visitNextPage = function () {
            var loginRole = localStorage.getItem('loginRole');
            var url = loginRole != Auth.ADMIN ? '/chats' : '/portal';
            $location.path(url);
        }

        $scope.registerDevice = function () {
            var deviceName = new Fingerprint().get();
            var userID = localStorage.getItem('userID');

            Auth.isDeviceRegisteredForUser(userID, deviceName).then(function (status) {
                if (!status) {
                    $location.path("/authenticate");
                } else {
                    localStorage.setItem('isDeviceAuthorized', true);
                    $scope.visitNextPage();
                }
            });
        }
    });
