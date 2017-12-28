angular.module('SECP')
    .controller('LoginController', function ($scope, Auth, $location, $webCrypto) {
        $scope.login = function () {

            Auth.login($scope.user).then(function(res) {
                if(res.status == 200) {
                    swal('Welcome to SECP!', 'Login successful!','success');
                    var loginRole = localStorage.getItem('loginRole');
                    var url = loginRole != Auth.ADMIN ? '/chats' : '/portal'
                    $location.path(url);
                    $scope.registerDevice();
                } else {
                    swal('Oops..!', res.data.message,'error');
                }
            });
        }

        $scope.registerDevice = function() {
            var deviceName = new Fingerprint().get();
            var username = localStorage.getItem('username');
            var userID = localStorage.getItem('user');

            Auth.isDeviceRegisteredForUser(userID, deviceName).then(function(status) {
                if(!status) {

                    //creating the user's private and public key
                    $webCrypto.generate({name: username})
                    .success(function(userKey) {
                        //Getting the user's public key.
                        publicKeyForUser = $webCrypto.export(userKey);
                        var req = {
                            "deviceName" : deviceName,
                            "publicKey" : publicKeyForUser,
                            "userID" : userID
                        };

                        Auth.addPublicKey(req).then(function(res) {
                            if(res.status == 201) {
                                localStorage.setItem('deviceID', res.data.id);
                            } else {
                                swal('Oops..!', "This device is not supported for chat messages",'error');
                            }
                        });
                    });
                }
            });
        }
    });
