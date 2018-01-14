angular.module('SECP')
    .controller('LoginController', function ($scope, Auth, $location, uuid) {
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
            var username = localStorage.getItem('username');
            var userID = localStorage.getItem('userID');

            Auth.isDeviceRegisteredForUser(userID, deviceName).then(function (status) {
                if (!status) {
                    //generate a RSA key pair
                    //Passphrase generated from username and random numbers.
                    var PassPhrase = uuid.v4() + username;

                    // The length of the RSA key, in bits.
                    var Bits = 1024;

                    var userPrivatekey = cryptico.generateRSAKey(PassPhrase, Bits);
                    var publicKeyForUser = cryptico.publicKeyString(userPrivatekey);
                    var req = {
                        "deviceName": deviceName,
                        "publicKey": publicKeyForUser,
                        "userID": userID
                    };

                    Auth.addPublicKey(req).then(function (res) {
                        if (res.status == 201) {
                            var flatten = function(obj) {
                                var result = Object.create(obj);
                                for(var key in result) {
                                    result[key] = result[key];
                                }
                                return result;
                            }
                            var obj = {userID: userID, key: userPrivatekey};

                            console.log(obj);
                            localforage.setItem(username, obj, function(err){
                                console.log(err);
                            });
                        } else {
                            swal('Oops..!', "This device is not supported for chat messages", 'error');
                        }
                    });
                }
            });
        }
    });
