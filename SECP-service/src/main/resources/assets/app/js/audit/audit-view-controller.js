angular.module('SECP')
    .controller('AuditViewController', function ($scope, Admin, $location, EncryptionService) {

        let routeParams = $location.search();

        if (!_.isEmpty(routeParams)) {
            $scope.name = routeParams.name;
            console.log(routeParams.groups);
            EncryptionService.getDecryptedSecretKeys().then(function (userSecretKeys) {
                if (userSecretKeys) {
                    $scope.secretKeysForChat = userSecretKeys;
                }
                $scope.contacts = routeParams.groups;
                $scope.currentUser = routeParams.auditUser;
            });

            $scope.contactSelected = function (contact) {
                $scope.selectedChat = contact;
                $scope.messages = contact.messages.reverse();
            };
        } else {
            swal("Audit failed", "please select an audit mode", "error");
            $location.path("/portal/audit");
        }
    });

