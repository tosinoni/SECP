angular.module('SECP')
    .controller('AuditLedgerController', function ($scope, Admin) {
        $scope.headers = ['Editor Name', 'Action Type', 'Editor Action', 'time'];
        $scope.ledger = [];

        Admin.getLedger().then(function (ledger) {
            console.log(ledger);
            if(ledger) {
                $scope.ledger = ledger;
            }
        })
    });
