angular.module('SECP')
    .controller('GroupController', function ($scope, Admin, SwalService) {
        $scope.groups = [];
        $scope.groupHeaders = ['Name', 'Permission Level(s)', 'Role(s)', 'Participants'];
        $scope.createGroupData = {};
        $scope.editGroupData = {};

        // //getting the groups
        Admin.getAllGroups().then(function(res) {
            if (res) {
                 $scope.groups = res;
            }
        });

        $scope.addGroup = function(row) {
            Admin.addGroup(row).then(function(res){
                if (res.status == 201) {
                    $scope.createGroupData = {};
                    $scope.groups.push(res.data);
                    swal('Added!','New group added.','success');
                } else {
                    swal('Oops!', res.data.message, "error");
                }
                $('#groupModal').modal('toggle');
            });
        };

         $scope.editGroupModalFn = function(row) {
             if (row) {
                console.log(row);
                 Admin.getGroup(row.groupID).then(function(res){
                     if (res) {
                        $scope.editGroupData = res;
                     }
                     $('#editGroupModal').modal('toggle');
                 })
             }
         };

        $scope.submitModifyGroup = function(row) {
            if (row) {
                Admin.editGroup(row).then(function(res){
                    if (res) {
                        var index = $scope.groups.indexOf(row);
                        $scope.groups[index] = res;
                        swal('Modified!','Group modified.','success');
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                    $('#editGroupModal').modal('toggle');
                })
            }
        };
    });
