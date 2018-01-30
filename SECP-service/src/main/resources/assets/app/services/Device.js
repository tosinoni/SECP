'use strict';

angular.module('SECP')
    .factory('Device', function($http) {
        return {
            getDevicesForGroup : function(groupID) {
                return $http.get("/SECP/device/group/" + groupID)
                .then(function(res) {
                    if(res.status == 200) {
                        return res.data;
                    }
                }, function(err) {
                    return err;
                });
            },

            getDevicesForUser : function(userID) {
                return $http.get("/SECP/device/user/" + userID)
                .then(function(res) {
                    if(res.status == 200) {
                        return res.data;
                    }
                }, function(err) {
                    return err;
                });
            },

            getDevicesForAdmins : function() {
                return $http.get("/SECP/device/admins/")
                .then(function(res) {
                    if(res.status == 200) {
                        return res.data;
                    }
                }, function(err) {
                    return err;
                });
            },

            getSecretKeysForDevice : function() {
                var deviceName = new Fingerprint().get();
                return $http.get("/SECP/device/" + deviceName + "/secret")
                    .then(function(res) {
                        if(res.status == 200) {
                            return res.data;
                        }
                    }, function(err) {
                        return err;
                    });
            },

            sendSecretKeyToDevices : function(devices) {
                return $http.post("/SECP/device/secret/", devices)
                .then(function(res) {
                    console.log(res);
                    if(res.status != 200) {
                        swal("Oops..", "could not send secret key to devices.", "error");
                    }
                }, function(err) {
                    return err;
                });
            }
        }
    });
