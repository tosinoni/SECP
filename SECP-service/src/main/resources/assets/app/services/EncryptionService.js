'use strict';

angular.module('SECP')
    .factory('EncryptionService', function($scope, Socket, Device, $q) {

        decryptSecretKeysForDevice();
        function sendSecretKeyToDevices (groupID, devices) {
            var deviceDTOS = [];
            var secretKey = cryptico.generateAESKey();

            for (var device of devices) {
                var secretObj = {};
                if (device.publicKey) {
                    //encrypt the secret key with the users public key
                    var EncryptionResult = cryptico.encrypt(secretKey, device.publicKey);
                    secretObj.groupID = groupID;
                    secretObj.deviceID = device.deviceID;
                    secretObj.encryptedSecret = EncryptionResult.cipher;
                    secretObj.userID = device.userID;
                    deviceDTOS.push(secretObj);
                }
            }

            Device.sendSecretKeyToDevices(deviceDTOS).then(function(res) {
                if (!res) {
                    console.log("could not send secret key to devices.");
                }
            });
        }

        function decryptSecretKeysForDevice() {
                Device.getSecretKeysForDevice().then(function (secretDTOS) {
                    if (secretDTOS) {
                        var obj = {};
                        var userID = localStorage.getItem('userID');
                        localforage.getItem(userID, function (err, userObj) {
                            var privateKey = cryptico.fromJSON(userObj.keypair);

                            for (var secretDTO of secretDTOS) {
                                var decryptedKey = cryptico.decrypt(secretDTO.encryptedSecret, privateKey);
                                if(decryptedKey.status !== "failure") {
                                    obj[secretDTO.groupID] = decryptedKey;
                                }
                            }

                            $scope.userSecretKeys = obj;
                        })
                    }
                });
        }

        function sendSecretKeysToUser(user) {
            if (user.loginRole == "ADMIN") {
                //send all the group key to the user
            } else {
                
            }
        }

        return {
            generateKeyPair: function (userID) {
                // The passphrase used to repeatably generate this RSA key.
                var PassPhrase = userID + " is a member of SECP.";

                // The length of the RSA key, in bits.
                var Bits = 1024;

                //key pair for user
                var keypair = cryptico.generateRSAKey(PassPhrase, Bits);

                var userObj = {keypair:  cryptico.toJSON(keypair)};

                localforage.setItem(userID, userObj);
                return keypair;
            },

            sendSecretKeysToGroup: function (groupID) {
                Device.getDevicesForGroup(groupID).then(function(devices) {
                    if (devices) {
                        sendSecretKeyToDevices(groupID, devices);
                    }
                });
            },

            getDecryptedSecretKeys: decryptSecretKeysForDevice
        }
    });
