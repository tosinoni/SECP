angular.module('SECP')
    .controller('ConfigureController', function ($scope, Admin, SwalService) {
        $scope.roles = [];
        $scope.permissions = [];

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

        $scope.deletePermission = function(permission) {
            var deletePermissionFunction = function () {
                Admin.deletePermission(permission.id).then(function(res){
                    if (res.status == 200) {
                        var index = $scope.permissions.indexOf(permission);
                        $scope.permissions.splice(index, 1);
                        swal('Deleted!','Permission level deleted.','success');
                    } else {
                        swal('Oops!', res.data, "error");
                    }
                });
            };
            SwalService.delete(deletePermissionFunction);
        };

        $scope.deleteRole = function(role) {
            var deleteRoleFunction = function () {
                Admin.deleteRole(role.id).then(function(res){
                    if (res.status == 200) {
                        var index = $scope.roles.indexOf(role);
                        $scope.roles.splice(index, 1);
                        swal('Deleted!','Role deleted.','success');
                    } else {
                        swal('Oops!', res.data, "error");
                    }
                });
            };
            SwalService.delete(deleteRoleFunction);
        };

        $scope.savePermission = function() {
            console.log($scope.permissionInput);
            if ($scope.permissionInput) {
                var permissions = $scope.permissionInput.split(/[ ,]+/);
                Admin.addPermissions(permissions).then(function(res){
                    if (res.status == 201) {
                        $scope.permissions = $scope.permissions.concat(res.data);
                        swal('Added!', 'permission was successfully added', "success");
                    } else {
                        swal('Oops!', res.data, "error");
                    }
                    $('#permissionModal').modal('toggle');
                    $scope.permissionInput = '';
                })
            }
        };

        $scope.saveRole = function() {
            console.log($scope.roleInput);
            if ($scope.roleInput) {
                var roles = $scope.roleInput.split(/[ ,]+/);
                Admin.addRoles(roles).then(function(res){
                    if (res.status == 201) {
                        $scope.roles = $scope.roles.concat(res.data);
                        swal('Added!', 'roles was successfully added', "success");
                    } else {
                        swal('Oops!', res.data, "error");
                    }
                    $('#roleModal').modal('toggle');
                    $scope.roleInput = '';
                });
            }
        };
    });
