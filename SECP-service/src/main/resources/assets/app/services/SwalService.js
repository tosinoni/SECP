'use strict';

angular.module('SECP')
  .factory('SwalService', function() {


    return {
        delete : function(callback) {
            swal({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result) {
                    callback();
                }
            }).catch((result) => {});
        }
    }
  });
