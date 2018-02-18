angular.module('SECP')
    .controller('AuditLedgerController', function ($scope, Admin, DTOptionsBuilder) {
        $scope.headers = ['Editor Name', 'Action Type', 'Editor Action', 'time'];
        $scope.ledger = [];

        $scope.dtOptions = DTOptionsBuilder.newOptions();

        Admin.getLedger().then(function (ledger) {
            console.log(ledger);
            if(ledger) {
                $scope.ledger = ledger;
            }
        });

        $scope.getTime = function(time) {
            if(time) {
                var date = new Date(time);
                return moment(date).fromNow();
            }
        }
    });
