'use strict';

angular.module('SECP')
    .factory('EncryptionService', function(Socket) {

        function getSecretKeyMessageForDevices (devices, secretKey) {
            var deviceObj = {};
            var promises = [];
            for (var device of devices) {
                if (device.publicKey) {
                    console.log(device.publicKey);
                    //encrypt the secret key with the users public key
                    var EncryptionResult = cryptico.encrypt(secretKey, device.publicKey);
                    deviceObj[device.deviceName]  = EncryptionResult.cipher;
                }
            }

            console.log(deviceObj);
            return deviceObj;
        }

        function sendMessage(senderID, message, groupID) {
            var messageDTO = {
                groupId: groupID,
                senderId: senderID,
                body: JSON.stringify(message),
                reason: "mayday"
            }

            console.log(messageDTO);
            Socket.send(messageDTO);
        }

        function RSAParse (rsaString) {
            var json = rsaString;
            var rsa = new RSAKey();

            console.log(json);
            rsa.setPrivateEx(json.n, json.e, json.d, json.p, json.q, json.dmp1, json.dmq1, json.coeff);

            return rsa;
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

            sendSecretKeysToGroup: function (senderID, group) {
                if(group && group.users) {
                    var message = {};
                    var secretKey = cryptico.generateAESKey();
                    console.log(secretKey);
                    var users = group.users;
                    for (var user of users) {
                        var devices = user.devices;
                        if (devices) {
                            var deviceObj = getSecretKeyMessageForDevices(devices, secretKey);
                            message[user.userID] = deviceObj;
                        }
                    }

                    sendMessage(senderID, message, group.groupID);
                }
            },

            storeSecretKeyForGroup: function(message, groupID) {

                var username = localStorage.getItem("username");
                localforage.getItem(username, function(err, userObj){
                    if(userObj) {
                        var userID = userObj.userID;
                        var deviceName = new Fingerprint().get();
                        var encryptedSecretKey = message[userID][deviceName];

                        console.log(RSAParse(userObj.key));
                        if(encryptedSecretKey) {
                            var decryptedSecretKey = cryptico.decrypt(encryptedSecretKey, RSAParse(userObj.key)).plaintext;

                            userObj[groupID] = decryptedSecretKey;
                            localforage.setItem(username, userObj, function(err){

                            });
                            console.log(userObj);
                        }
                    }
                });
            }
        }
    });
