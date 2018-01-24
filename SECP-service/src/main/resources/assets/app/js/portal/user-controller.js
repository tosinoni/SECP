angular.module('SECP')
    .controller('UserController', function ($scope, Admin, SwalService, EncryptionService) {
        $scope.users = [];
        $scope.userHeaders = ['Username', 'First Name', 'Last Name', 'Permission Level', 'Role(s)', 'Groups', 'Login Role'];
        $scope.createUserData = {}; //the data sent to the modal for create user
        $scope.editUserData = {};   //the data sent to the modal for edit user

        //getting all the users to populate the table
        Admin.getAllUsers().then(function(res) {
            if (res) {
                $scope.users = res;
            }
        });

        //this function handles the information provided by the create user modal
        $scope.register = function (user) {
            Admin.register(user).then(function(res){
                if (res.status == 201) {
                    $scope.createUserData = {};
                    $scope.users.push(res.data);
                    swal('Added!','New user added.','success');
                    EncryptionService.sendSecretKeysToUser(res.data);
                } else {
                    swal('Oops!', res.data.message, "error");
                }
                $('#registerModal').modal('toggle');
            });
        };

        //this function gets the data to populate the modal and open the modal
        $scope.editUserModalFn = function(row) {
            if (row) {
                Admin.getUser(row.userID).then(function(res){
                    if (res) {
                        $scope.editUserData = res;
                    }
                    $('#editUserModal').modal('toggle');
                })
            }
        };

        //this function handles the information provided by the edit modal
        $scope.submitModifyUser = function(row) {
            if (row) {
                Admin.editUser(row).then(function(res){
                    if (res.status == 200) {
                        var index = _.findIndex($scope.users, function(o) { return o.username == row.username; });
                        $scope.users[index] = res.data;
                        swal('Modified!','User modified.','success');
                        EncryptionService.sendSecretKeysToUser(res.data);
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                    $('#editUserModal').modal('toggle');
                })
            }
        };

        //delete group
        $scope.deleteUser = function(row) {
            var deleteUserFunction = function () {
                Admin.deleteUser(row.userID).then(function(res){
                    if (res.status == 200) {
                        //TODO need a way to show that the user is deactivated
                        swal('Deleted!','User deactivated.','success');
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                });
            };

            var deleteStatement = 'Enter the username of the user to delete';
            SwalService.deleteImportantInformation(row.username, deleteStatement, deleteUserFunction);
        };
    });
