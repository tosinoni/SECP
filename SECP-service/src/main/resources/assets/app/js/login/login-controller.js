angular.module('SECP')
    .controller('LoginController', ['$scope', 'Auth', '$location', function ($scope, Auth, $location) {
        $scope.login = function () {
            Auth.login($scope.user).then(function(res) {
                if(res.status == 200) {
                    swal('Welcome to SECP!', 'Login successful!','success');
                    var loginRole = localStorage.getItem('loginRole');
                    var url = loginRole != Auth.ADMIN ? '/chats' : '/portal'
                    $location.path(url);
                } else {
                    swal('Oops..!', res.data.message,'error');
                }
            });
        }
    }]);
