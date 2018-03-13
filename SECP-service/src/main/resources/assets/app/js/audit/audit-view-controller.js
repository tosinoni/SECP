angular.module('SECP')
    .controller('AuditViewController', function ($scope, Admin, $location, EncryptionService, Chat) {

        let routeParams = $location.search();

        if (!_.isEmpty(routeParams)) {
            $scope.name = routeParams.name;
            EncryptionService.getDecryptedSecretKeys().then(function (userSecretKeys) {
                if (userSecretKeys) {
                    $scope.secretKeysForChat = userSecretKeys;
                }
                $scope.contacts = routeParams.groups;
                $scope.currentUser = routeParams.auditUser;
            });

            $scope.contactSelected = function (contact) {
                Chat.getMessages(contact).then(function(data) {
                    if(data) {
                        $scope.messages = data.reverse();
                    }
                });
                $scope.selectedChat = contact;
            };
        } else {
            swal("Audit failed", "please select an audit mode", "error");
            $location.path("/portal/audit");
        }
    });

