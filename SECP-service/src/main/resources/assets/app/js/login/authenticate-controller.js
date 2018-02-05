angular.module('SECP')
    .controller('AuthenticateController', function ($scope, Device, EncryptionService) {
        var userID = localStorage.getItem('userID');
        Device.getDevicesForUser(userID).then(function (devices) {
            if (devices && devices.length > 0) {
                $scope.isDevice = true;
                $scope.devices = devices;
            }
        });

        Device.getDevicesForAdmins().then(function (adminDevices) {
            if (adminDevices && adminDevices.length > 0) {
                $scope.adminDevices = adminDevices;
            } else {
                EncryptionService.registerDefaultUser(userID);
            }
        });

        $scope.authorizeByUserDevice = function () {
            EncryptionService.getApprovalFromUserDevice(userID, "user_authorization");
            swal("Authorization requested",
                "Please confirm on your other device.",
                "success");
        }

        $scope.authorizeByAdmin = function () {
            console.log($scope.adminDevices);
            EncryptionService.getApprovalFromUserDevice(userID, "admin_authorization");
            swal("Authorization requested", "Please contact an admin", "success");
        }
    });
