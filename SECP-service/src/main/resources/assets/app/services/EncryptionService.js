'use strict';

angular.module('SECP')
    .factory('EncryptionService', function(Socket, Device) {

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
            var deferred = $q.defer();

            setTimeout(function() {
                Device.getSecretKeysForDevice().then(function (keys) {
                    if (keys) {
                        var obj = {};
                        localforage.getItem(userID, function (err, userObj) {
                            var privateKey = cryptico.fromJSON(userObj.keypair);
                            for (var key of keys) {
                                obj[key.groupID] = cryptico.decrypt(key, privateKey);
                            }

                            deferred.resolve(obj);
                        })
                    }
                });
            }, 200);

            return deferred.promise;
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
