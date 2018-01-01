angular.module('SECP')
    .controller('FilterController', ['$scope', function ($scope) {
        var filtertable =$('#filtertable').DataTable({
            //Need drawCallBack to take load select fields on every draw event of the table
            drawCallback: function() {
                $('.permission-level-select').select2();
                $('.role-select').select2();
            }
        });

        $scope.delete = function() {
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
                filtertable.row($(self).parents('tr')).remove().draw(false);
                swal('Deleted!','The filtered keyword has been deleted.','success')
              }
            }).catch((result) => {});
        }

    }]);
