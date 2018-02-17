angular.module('SECP')
    .controller('AuditGroupController', function ($scope, Admin, $location, Chat) {
        $scope.isCollapsed = true;
        $scope.isGroupAuditCollapsed = true;


        Admin.getAllGroups().then(function (groups) {
            $scope.groups = angular.copy(groups);
        });

        Chat.getCurrentUser().then(function(user) {
            if(user) {
                $scope.currentUser = user;
            }
        });

        //Ensures the admin is not able to audit without specifying a group
        $scope.validateGroupAudit = function() {
            if (!$scope.audit || !$scope.audit.groups) {
                swal({
                    position: 'top-right',
                    type: 'error',
                    title: 'Please specify a group to audit',
                    showConfirmButton: false,
                    timer: 1500
                }).catch((result) => {});
            } else {
                Admin.auditGroup($scope.audit).then(function (res) {
                    if(res.status !== 200) {
                        swal("Audit request failed", res.data.message, "error");
                    } else {
                        console.log(res.data);
                        $location.path('/portal/audit/view').search({
                            groups: res.data,
                            name: "Group Audit View",
                            auditUser: $scope.currentUser
                        });
                        swal("Yaah", "Audit request successful", "success");
                    }
                })
            }
        }
        //Populates the advanced search time for user auditing
        $('input[name=todate]').val(moment().format('YYYY-MM-DDTHH:mm'));
    });
