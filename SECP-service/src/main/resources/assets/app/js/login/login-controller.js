angular.module('SECP')
    .controller('LoginController', function ($scope, Auth, $location, EncryptionService) {
        $scope.login = function () {
            // TODO: NEED TO SEND AUTHENTICATION EMAIL (IF REQUIRED BY ADMIN) CONTAINING VERIFICATION CODE WHEN SUCCESSFULLY LOGGED IN
            Auth.login($scope.user).then(function (res) {
                if (res.status == 200) {
                    swal('Welcome to SECP!', 'Login successful!', 'success');
                    var loginRole = localStorage.getItem('loginRole');
                    var url = loginRole != Auth.ADMIN ? '/chats' : '/portal'
                    $location.path(url);
                    $scope.registerDevice();
                } else {
                    swal('Oops..!', res.data.message, 'error');
                }
            });
        }

        $scope.registerDevice = function () {
            var deviceName = new Fingerprint().get();
            var userID = localStorage.getItem('userID');

            Auth.isDeviceRegisteredForUser(userID, deviceName).then(function (status) {
                if (!status) {
                    var keypairForUser = EncryptionService.generateKeyPair(userID);
                    //Getting the user's public key.
                    let publicKeyForUser = cryptico.publicKeyString(keypairForUser);
                    var req = {
                        "deviceName": deviceName,
                        "publicKey": publicKeyForUser,
                        "userID": userID
                    };

                    Auth.addPublicKey(req).then(function (res) {
                        if (res.status !== 201) {
                            swal('Oops..!', "This device is not supported for chat messages", 'error')
                        }
                    })
                }
            });
        }
    });
