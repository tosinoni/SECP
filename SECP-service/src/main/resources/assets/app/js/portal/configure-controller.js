angular.module('SECP')
    .controller('ConfigureController', ['$scope', function ($scope) {
        var permissiontable =$('#permissionTable').DataTable();
        var roletable =$('#rolesTable').DataTable();
        var permissions = document.getElementById("permissionTableDiv");
        var roles = document.getElementById("rolesTableDiv");
        var optionline1 = document.getElementById("optionline1");
        var optionline2 = document.getElementById("optionline2");

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

        $scope.deletePermission = function() {
            var self= this;
            swal({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            })
                .then((result) => {
                    if (result) {
                        permissiontable.row($(self).parents('tr')).remove().draw(false);
                        swal('Deleted!','Permission level deleted.','success')
                    }
                }).catch((result) => {});
        };

        $scope.deleteRole = function() {
            var self= this;
            swal({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            })
                .then((result) => {
                    if (result) {
                        permissiontable.row($(self).parents('tr')).remove().draw(false);
                        swal('Deleted!','Role deleted.','success')
                    }
                }).catch((result) => {});
        };

    }]);
