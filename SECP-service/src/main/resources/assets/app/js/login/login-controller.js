angular.module('SECP')
    .controller('LoginController', ['$scope', 'Auth', '$location', function ($scope, Auth, $location) {
        $scope.login = function () {
            Auth.login($scope.user).then(function(res) {
                if(res.status == 200) {
                    swal('Welcome to SECP!', 'Login successful!','success');
                    $location.path('/chats');
                } else {
                    swal('Oops..!', res.data.message,'error');
                }
            });
        }
    }]);
