angular.module('SECP')
    .controller('GroupController', function ($scope, Admin, SwalService) {
        $scope.groups = [];
        $scope.groupHeaders = ['Name', 'Display Name', 'Permission Level(s)', 'Role(s)', 'Participants'];

        // //getting the groups
        // Admin.getGroups().then(function(res) {
        //     if (res) {
        //         $scope.groups = res;
        //     }
        // })
        //
        // //TODO
        // $scope.addGroup = function(row) {
        //     var addGroupFunction = function () {
        //         Admin.deletePermission(row.id).then(function(res){
        //             if (res.status == 200) {
        //                 var index = $scope.permissions.indexOf(row);
        //                 $scope.permissions.splice(index, 1);
        //                 swal('Deleted!','Permission level deleted.','success');
        //             } else {
        //                 swal('Oops!', res.data, "error");
        //             }
        //         });
        //     };
        //     SwalService.delete(deletePermissionFunction);
        // };
        //
        // $scope.deleteGroup = function(row) {
        //     var deleteGroupFunction = function () {
        //         Admin.deleteGroup(row.id).then(function(res){
        //             if (res.status == 200) {
        //                 var index = $scope.groups.indexOf(row);
        //                 $scope.groups.splice(index, 1);
        //                 swal('Deleted!','Group deleted.','success');
        //             } else {
        //                 swal('Oops!', res.data, "error");
        //             }
        //         });
        //     };
        //     SwalService.delete(deleteGroupFunction);
        // };
        //
        // $scope.editGroup = function() {
        //     console.log($scope.permissionInput);
        //     if ($scope.permissionInput) {
        //         var permissions = $scope.permissionInput.split(/[ ,]+/);
        //         Admin.addPermissions(permissions).then(function(res){
        //             if (res.status == 201) {
        //                 $scope.permissions = $scope.permissions.concat(res.data);
        //                 swal('Added!', 'permission was successfully added', "success");
        //             } else {
        //                 swal('Oops!', res.data, "error");
        //             }
        //             $('#permissionModal').modal('toggle');
        //             $scope.permissionInput = '';
        //         })
        //     }
        // };
    });
