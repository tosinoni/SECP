angular.module('SECP')
    .controller('GroupController', function ($scope, Admin, SwalService) {
        $scope.groups = [];
        $scope.groupHeaders = ['Name', 'Permission Level(s)', 'Role(s)', 'Participants'];
        $scope.createGroupData = {}; //the data sent to the modal for create group
        $scope.editGroupData = {};   //the data sent to the modal for edit group

        //getting all the groups to populate the table
        Admin.getAllGroups().then(function(res) {
            if (res) {
                 $scope.groups = res;
            }
        });

        //this function handles the information provided by the create modal
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

         //this function gets the data to populate the modal and open the modal
         $scope.editGroupModalFn = function(row) {
             if (row) {
                 Admin.getGroup(row.groupID).then(function(res){
                     if (res) {
                        $scope.editGroupData = res;
                     }
                     $('#editGroupModal').modal('toggle');
                 })
             }
         };

        //this function handles the information provided by the edit modal
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
