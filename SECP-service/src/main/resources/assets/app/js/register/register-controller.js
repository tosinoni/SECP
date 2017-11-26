angular.module('SECP')
    .controller('RegisterController', ['$scope', 'RegisterService',
        function ($scope, RegisterService) {

        $scope.$watch("confirmedPassword", function(confirmedPassword) {
            var v = false;

            if($scope.user) {
                v = $scope.user.password === confirmedPassword;
            }
            $scope.frm.confirmedPassword.$setValidity("unique", v);
        });

        $scope.register = function () {
            RegisterService.register($scope.user).then(function(res) {
                if(res.status == 400) {
                    console.log(res.data);
                }

            });
        }
    }]);
