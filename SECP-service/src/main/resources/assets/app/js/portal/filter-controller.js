angular.module('SECP')
    .controller('FilterController', ['$scope', function ($scope) {
        var filtertable =$('#filtertable').DataTable({
            //Need drawCallBack to take load select fields on every draw event of the table
            drawCallback: function() {
                $('.permission-level-select').select2();
                $('.role-select').select2();
            }
        });

        $('#filtertable tbody').on('click', 'button[data-target="#delete"]', function () {
            tablerow= this;
            //Replace with SweetAlert
            if (window.confirm("Are you sure you want to delete this word?")) {
                filtertable.row($(tablerow).parents('tr')).remove().draw(false);
            }
        });

    }]);
