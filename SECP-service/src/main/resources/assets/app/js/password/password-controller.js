angular.module('SECP')
    .controller('PasswordController', ['$scope', '$window', function ($scope, $window) {
        $scope.password_conf = null;
        $scope.sendEmail = function () {
            swal('email sent.');    //TO DO ADD SERVER SIDE LOGIC TO SEND EMAIL
        }
    }]);
    