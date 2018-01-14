angular.module('SECP')
    .controller('FilterController', function ($scope, Admin, SwalService) {
        $scope.filters = [];
        $scope.filterHeaders = ['Filter Word', 'Permission Level(s)', 'Role(s)'];
        $scope.createFilterData = {}; //the data sent to the modal for create new filter word

        //getting all the filters to populate the table
        Admin.getAllFilters().then(function(res) {
            if (res) {
                $scope.filters = res;
            }
        });

        //this function handles the information provided by the create modal
        $scope.addFilter = function(row) {
            Admin.addFilter(row).then(function(res){
                if (res.status == 201) {
                    $scope.createFilterData = {};
                    $scope.filters.push(res.data);
                    swal('Added!','New filter word added.','success');
                } else {
                    swal('Oops!', res.data.message, "error");
                }
                $('#filterModal').modal('toggle');
            });
        };

        //delete filter word
        $scope.deleteFilter = function(row) {
            var deleteFilterFunction = function () {
                Admin.deleteFilter(row.id).then(function(res){
                    if (res.status == 200) {
                        var index = $scope.filters.indexOf(row);
                        $scope.filters.splice(index, 1);
                        swal('Deleted!','Filter word deleted.','success');
                    } else {
                        swal('Oops!', res.data.message, "error");
                    }
                });
            };
            SwalService.delete(deleteFilterFunction);
        };
    });
