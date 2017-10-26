angular.module('SECP')
    .controller('HomeController', ['$scope', function ($scope) {

        $scope.displayLoginForm = function () {
            $scope.getElementById('register').className = 'hiddenForm';
            $scope.getElementById('login').classList.remove('hiddenForm');
            console.log('hi');
        };

        $scope.displayRegisterForm = function () {
            $scope.getElementById('login').className = 'hiddenForm';
            $scope.getElementById('register').classList.remove('hiddenForm');
            console.log('hi2');
        };
    }]);