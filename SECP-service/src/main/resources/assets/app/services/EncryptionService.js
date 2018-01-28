'use strict';

angular.module('SECP')
    .factory('EncryptionService', function(Socket, Device, $q) {
        function getSecretKeyToSendToDevices (groupID, devices, secretKey) {
            let deviceDTOS = [];

            for (let device of devices) {
                let secretObj = {};
                if (device.publicKey) {
                    //encrypt the secret key with the users public key
                    let EncryptionResult = cryptico.encrypt(JSON.stringify(secretKey), device.publicKey);
                    secretObj.groupID = groupID;
                    secretObj.deviceID = device.deviceID;
                    secretObj.encryptedSecret = EncryptionResult.cipher;
                    secretObj.userID = device.userID;
                    deviceDTOS.push(secretObj);
                }
            }

            return deviceDTOS;
        }

        function getDecryptedSecretKeys() {
            let deferred = $q.defer();

            setTimeout(function() {
                Device.getSecretKeysForDevice().then(function (secretDTOS) {
                    console.log(secretDTOS);
                    if (secretDTOS) {
                        let obj = {};
                        let userID = localStorage.getItem('userID');
                        localforage.getItem(userID, function (err, userObj) {
                            let privateKey = cryptico.fromJSON(userObj.keypair);
                            for (let secretDTO of secretDTOS) {
                                let decryptedKey = cryptico.decrypt(secretDTO.encryptedSecret, privateKey);
                                if (decryptedKey.status !== "failure") {
                                    obj[secretDTO.groupID] = JSON.parse(decryptedKey.plaintext);
                                } else {
                                    swal("Oops..", "Cannot decrypt secret keys using private key", "error");
                                    break;
                                }
                            }

                            deferred.resolve(obj);
                        })
                    }
                });
            });

            return deferred.promise;
        }

        function sendSecretKeyToUserDevices(user, devices) {
            getDecryptedSecretKeys().then(function (userSecretKeys) {
                if(userSecretKeys) {
                    let isAdmin = user.loginRole === "ADMIN";
                    let groups = [];

                    if (isAdmin) {
                        //send all the group key to the user devices
                        groups = Object.keys(userSecretKeys);
                    } else if (user.groups) {
                        groups = user.groups.map(function (group) {
                            return group.groupID;
                        });
                    }

                    let secretDTOS = [];

                    for (let groupID of groups) {
                        let secretKeyForGroup = userSecretKeys[groupID];
                        secretDTOS = secretDTOS.concat(getSecretKeyToSendToDevices(groupID, devices, secretKeyForGroup));
                    }

                    Device.sendSecretKeyToDevices(secretDTOS);
                }
            });
        }

        return {
            generateKeyPair: function (userID) {
                // The passphrase used to repeatably generate this RSA key.
                let PassPhrase = userID + " is a member of SECP.";

                // The length of the RSA key, in bits.
                let Bits = 1024;

                //key pair for user
                let keypair = cryptico.generateRSAKey(PassPhrase, Bits);

                return keypair;
            },

            sendSecretKeysToGroup: function (groupID) {
                Device.getDevicesForGroup(groupID).then(function(devices) {
                    if (devices) {
                        let secretKey = cryptico.generateAESKey();
                        let secretDTOS = getSecretKeyToSendToDevices(groupID, devices, secretKey);
                        Device.sendSecretKeyToDevices(secretDTOS);
                    }
                });
            },

            sendSecretKeysToUser: function (user) {
                Device.getDevicesForUser(user.userID).then(function (devices) {
                    if (devices && devices.length > 0) {
                        sendSecretKeyToUserDevices(user, devices);
                    }
                });
            },

            enryptMessage: function (message, aesEncryptionKey) {
                if(message && aesEncryptionKey) {
                    return cryptico.encryptAESCBC(message, aesEncryptionKey);
                }
            },

            decryptMessage: function (encryptedMessage, aesDecryptionKey) {
                if(encryptedMessage && aesDecryptionKey) {
                    return cryptico.decryptAESCBC(encryptedMessage, aesDecryptionKey);
                }
            },

            getDecryptedSecretKeys: getDecryptedSecretKeys
        }
    });
