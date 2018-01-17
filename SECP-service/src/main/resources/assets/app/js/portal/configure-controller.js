angular.module('SECP')
    .controller('ConfigureController', function ($scope, Admin, SwalService) {
        $scope.roles = [];
        $scope.permissions = [];
        $scope.roleHeaders = ['Role', 'Color'];
        $scope.permissionHeaders = ['Permission Level', 'Color'];
        $scope.newPermissionObj = {};
        $scope.editPermissionObj = {};
        $scope.newRoleObj = {};
        $scope.editRoleObj = {};

        var permissions = document.getElementById("permissionTableDiv");
        var roles = document.getElementById("rolesTableDiv");
        var optionline1 = document.getElementById("optionline1");
        var optionline2 = document.getElementById("optionline2");

        //getting the adminRoles
        Admin.getRoles().then(function(res) {
            if (res) {
                $scope.roles = res;
            }
        })

        //getting the permissions
        Admin.getPermissions().then(function(res) {
            if (res) {
                $scope.permissions = res;
            }
        })

        $scope.permissionClick = function() {
            if (permissions.style.display === "none") {
                $("#optionicon1").text("-");
                permissions.style.display = "block";
                optionline1.style.display = "block";
                //Insert table after line in DOM
                $('#optionline1').after($('#permissionTableDiv'));
            } else {
                $("#optionicon1").text("+");
                permissions.style.display = "none";
                optionline1.style.display = "none";
            }
        };

        $scope.rolesClick = function() {
            if (roles.style.display === "none") {
                $("#optionicon2").text("-");
                roles.style.display = "block";
                optionline2.style.display = "block";
                //Insert table after line in DOM
                $('#optionline2').after($('#rolesTableDiv'));
            } else {
                $("#optionicon2").text("+");
                roles.style.display = "none";
                optionline2.style.display = "none";
            }
        };

        $scope.deletePermission = function(row) {
            var deletePermissionFunction = function () {
                Admin.deletePermission(row.id).then(function(res){
                    if (res.status == 200) {
                        var index = $scope.permissions.indexOf(row);
                        $scope.permissions.splice(index, 1);
                        swal('Deleted!','Permission level deleted.','success');
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                });
            };
            SwalService.delete(deletePermissionFunction);
        };

        $scope.deleteRole = function(row) {
            var deleteRoleFunction = function () {
                Admin.deleteRole(row.id).then(function(res){
                    if (res.status == 200) {
                        var index = $scope.roles.indexOf(row);
                        $scope.roles.splice(index, 1);
                        swal('Deleted!','Role deleted.','success');
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                });
            };
            SwalService.delete(deleteRoleFunction);
        };


        $scope.editPermissionModalFn = function(row) {
            $scope.editPermissionObj = row;
            $('#editPermissionModal').modal('toggle');
        };

        //this function gets the data to populate the modal and open the modal
        $scope.editRoleModalFn = function(row) {
            $scope.editRoleObj = row;
            $('#editRoleModal').modal('toggle');
        };

        $scope.savePermission = function(data) {
            if (data && data.name) {
                var permissions = data.name.split(/[ ,]+/);
                var obj = {permissions: permissions, color: data.color};
                console.log(obj);
                Admin.addPermissions(obj).then(function(res){
                    if (res.status == 201) {
                        $scope.permissions = $scope.permissions.concat(res.data);
                        swal('Added!', 'permission was successfully added', "success");
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                    $('#permissionModal').modal('toggle');
                    $scope.permissionInput = '';
                })
            }
        };

        //this function handles the information provided by the edit modal
        $scope.submitModifyPermission = function(data) {
            if (data) {
                Admin.modifyPermission(data).then(function(res){
                    console.log(res);
                    if (res.status == 201) {
                        var index = _.findIndex($scope.permissions, function(o) { return o.id == data.id; });
                        $scope.permissions[index] = res.data;
                        swal('Modified!','Permission modified.','success');
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                    $('#editPermissionModal').modal('toggle');
                })
            }
        };

        $scope.submitModifyRole = function(data) {
            if (data) {
                Admin.modifyRole(data).then(function(res){
                    if (res.status == 201) {
                        var index = _.findIndex($scope.roles, function(o) { return o.id == data.id; });
                        $scope.roles[index] = res.data;
                        swal('Modified!','Role modified.','success');
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                    $('#editRoleModal').modal('toggle');
                })
            }
        };

        $scope.saveRole = function(data) {
            if (data && data.name) {
                var roles = data.name.split(/[ ,]+/);
                var obj = {roles: roles, color: data.color};
                Admin.addRoles(obj).then(function(res){
                    if (res.status == 201) {
                        $scope.roles = $scope.roles.concat(res.data);
                        swal('Added!', 'roles was successfully added', "success");
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                    $('#roleModal').modal('toggle');
                    $scope.roleInput = '';
                });
            }
        };
    });
