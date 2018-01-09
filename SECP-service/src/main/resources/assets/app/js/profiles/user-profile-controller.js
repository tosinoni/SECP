angular.module('SECP')
    .controller('UserProfileController', ['$scope', function ($scope) {
        $scope.IsSaveVisible = false;
        $scope.IsDisplayNameReadOnly = true;
        $scope.IsImageFileVisible = false;
        $scope.IsEditAvailable = true;
        $scope.EditProfileAvailable = function () {
            let username = angular.element('#username').html();
            if (localStorage.getItem("username") == username) {
                return true;
            }
            else {
                return false;
            }
        }
        $scope.EditProfile = function () {
            $scope.IsSaveVisible = $scope.IsSaveVisible ? false : true;
            $scope.IsDisplayNameReadOnly = $scope.IsDisplayNameReadOnly ? false : true;
            $scope.IsImageFileVisible = $scope.IsImageFileVisible ? false : true;
        }
        $scope.SaveChanges = function () {
            $scope.IsSaveVisible = false;
            $scope.IsDisplayNameReadOnly = true;
            $scope.IsImageFileVisible = false;
        }
    }]);