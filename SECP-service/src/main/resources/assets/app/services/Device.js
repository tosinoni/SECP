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
                    if(res.status == 200) {
                        return res.data;
                    }
                }, function(err) {
                    return err;
                });
            }
        }
    });
