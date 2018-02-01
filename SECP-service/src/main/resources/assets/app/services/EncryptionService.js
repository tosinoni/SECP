'use strict';

angular.module('SECP')
    .factory('EncryptionService', function(Socket, Device, $q, SwalService, UserService, Auth, $location) {
        var requiredStatus = 'required';
        var approvedStatus = 'approved';

        function generateKeyPair (deviceName) {
            // The passphrase used to repeatably generate this RSA key.
            let PassPhrase = deviceName + " is a member of SECP.";

            // The length of the RSA key, in bits.
            let Bits = 1024;

            //key pair for user
            let keypair = cryptico.generateRSAKey(PassPhrase, Bits);

            return keypair;
        }

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
                if(!_.isEmpty(userSecretKeys)) {
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

        function getApprovalFromUserDevice(userID, approvalType) {
            let deviceName = new Fingerprint().get();
            let keypair = generateKeyPair(deviceName);

            let userObj = {keypair:  cryptico.toJSON(keypair)};
            localforage.setItem(userID, userObj).then(function () {
                let messageBody = {
                    publicKey: cryptico.publicKeyString(keypair),
                    status: requiredStatus,
                    deviceName: deviceName
                }

                let messageDTO = {
                    body: JSON.stringify(messageBody),
                    senderId: userID,
                    reason: approvalType
                }

                Socket.send(messageDTO);
            });
        }

        function sendAuthorizationApproval(userID, deviceName) {
            let messageBody = {
                status: approvedStatus,
                deviceName : deviceName
            }

            let messageDTO = {
                body: JSON.stringify(messageBody),
                senderId: userID,
                reason: "user_approved"
            }

            Socket.send(messageDTO);
        }

        function registerNewDevice(userID, deviceName, devicePublicKey) {
            let deferred = $q.defer();

            let req = {
                "deviceName": deviceName,
                "publicKey": devicePublicKey,
                "userID": userID
            };

            Auth.addNewDevice(req).then(function (res) {
                if (res.status == 201) {
                    deferred.resolve(res.data);
                } else {
                    deferred.reject();
                }
            })

            return deferred.promise;
        }

        function approveDevice(userID, deviceName, devicePublicKey) {
            UserService.getUser(userID).then(function (user) {
                if (user) {
                    registerNewDevice(userID, deviceName, devicePublicKey).then(function (newDevice) {
                        sendSecretKeyToUserDevices(user, [newDevice]);
                        sendAuthorizationApproval(userID, deviceName);
                    });
                }
            })
        }

        function visitNextPage() {
            localStorage.setItem('isDeviceAuthorized', true);
            var loginRole = localStorage.getItem('loginRole');
            var url = loginRole != Auth.ADMIN ? '/chats' : '/portal';
            document.location.href = url;
        }

        function handleAuthorizationRequest(messageObj) {
            let messageBody = JSON.parse(messageObj.body);
            let deviceName = new Fingerprint().get();

            if (messageBody.status === requiredStatus && deviceName !== messageBody.deviceName) {
                let callbackFn = function () {
                    approveDevice(messageObj.senderId, messageBody.deviceName, messageBody.publicKey)
                    swal("Yaah", "Device Approved", "success");
                }

                SwalService.authorizeUserSwal(deviceName, callbackFn);
            } else if (messageBody.status === approvedStatus && deviceName === messageBody.deviceName) {
                visitNextPage();
                swal("Yaah", "Device approved", "success");
            }
        }

        function registerDefaultUser(userID) {
            let deviceName = new Fingerprint().get();
            let keypair = generateKeyPair(deviceName);

            let userObj = {keypair:  cryptico.toJSON(keypair)};
            localforage.setItem(userID, userObj).then(function () {
                let devicePublicKey = cryptico.publicKeyString(keypair);
                registerNewDevice(userID, deviceName, devicePublicKey).then(function (newDevice) {
                    visitNextPage();
                });
            })
        }
        return {
            sendSecretKeysToGroup: function (groupID, secretKey) {
                Device.getDevicesForGroup(groupID).then(function(devices) {
                    if (devices) {
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

            getDecryptedSecretKeys: getDecryptedSecretKeys,
            registerDefaultUser: registerDefaultUser,
            getApprovalFromUserDevice: getApprovalFromUserDevice,
            handleAuthorizationRequest: handleAuthorizationRequest
        }
    });
