angular.module('SECP')
    .controller('LoginController', function ($scope, Auth, $location, EncryptionService, $rootScope) {
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
                    $rootScope.deviceAuthorized = false;
                    $location.path("/authenticate");
                    console.log($rootScope);
                    //var keypairForUser = EncryptionService.generateKeyPair(userID);

                    //let userObj = {keypair:  cryptico.toJSON(keypairForUser)};


                    // localforage.setItem(userID, userObj).then(function () {
                    //     //Getting the user's public key.
                    //     let publicKeyForUser = cryptico.publicKeyString(keypairForUser);
                    //     var req = {
                    //         "deviceName": deviceName,
                    //         "publicKey": publicKeyForUser,
                    //         "userID": userID
                    //     };
                    //
                    //     Auth.addPublicKey(req).then(function (res) {
                    //         if (res.status !== 201) {
                    //             swal('Oops..!', "This device is not supported for chat messages", 'error')
                    //         }
                    //     })
                    //
                    //     $scope.visitNextPage();
                    // });
                } else {
                    $rootScope.deviceAuthorized = true;
                    $scope.visitNextPage();
                }
            });
        }
    });
