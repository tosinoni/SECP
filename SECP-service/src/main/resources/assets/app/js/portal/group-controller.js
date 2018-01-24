angular.module('SECP')
    .controller('GroupController', function ($scope, Admin, SwalService, EncryptionService) {
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
                    $('#groupModal').modal('toggle');
                    EncryptionService.sendSecretKeysToGroup(res.data.groupID);
                } else {
                    swal('Oops!', res.data.message, "error");
                    $('#groupModal').modal('toggle');
                }
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
                    if (res.status == 201) {
                        var index = _.findIndex($scope.groups, function(o) { return o.groupID == row.groupID; });
                        $scope.groups[index] = res.data;
                        swal('Modified!','Group modified.','success');
                        EncryptionService.sendSecretKeysToGroup(res.data.groupID);
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                    $('#editGroupModal').modal('toggle');
                })
            }
        };

        //delete group
        $scope.deleteGroup = function(row) {
            var deleteGroupFunction = function () {
                console.log(row.groupID);
                Admin.deleteGroup(row.groupID).then(function(res){
                    if (res.status == 200) {
                        //TODO need a way to show that the group is deactivated
                        swal('Deleted!','Group deactivated.','success');
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                });
            };

            var deleteStatement = 'Enter the name of the group to delete';
            SwalService.deleteImportantInformation(row.name, deleteStatement, deleteGroupFunction);
        };
    });
