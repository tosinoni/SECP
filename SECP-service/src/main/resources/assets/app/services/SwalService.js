'use strict';

angular.module('SECP')
  .factory('SwalService', function() {

    var deleteSwal = function(callback) {
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
    };

    function verifyInformation(title, infoName, callback) {
        swal({
            title: title,
            input: 'text',
            showCancelButton: true,
            confirmButtonText: 'Submit',
            showLoaderOnConfirm: true,
            preConfirm: (inputValue) => {
                return new Promise((resolve, reject) => {
                    setTimeout(() => {
                        if (inputValue !== infoName) {
                            swal.showValidationError('input value does not match.');
                            reject();
                        }
                        resolve();
                    }, 100)
                });
            },
            allowOutsideClick: () => !swal.isLoading()
        }).then((result) => {
            if (result) {
                callback();
            }
        }).catch((result) => {});
    }

    function authorizeUserSwal(deviceName, callback) {
        let title = "Please enter these characters to verify: " + deviceName;
        swal({
            title: 'New device detected',
            text: "Do you want to trust this device?!",
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, Trust it!'
        }).then((result) => {
            if (result) {
                verifyInformation(title, deviceName.toString(), callback);
            }
        }).catch((result) => {});
    }

    return {
        delete : deleteSwal,
        deleteImportantInformation : function(infoName, deleteTitle, callback) {
            var deleteImportantInformationFunction = function () {
                swal({
                  title: deleteTitle,
                  input: 'text',
                  showCancelButton: true,
                  confirmButtonText: 'Submit',
                  showLoaderOnConfirm: true,
                  preConfirm: (inputValue) => {
                    return new Promise((resolve, reject) => {
                        setTimeout(() => {
                            if (inputValue !== infoName) {
                                swal.showValidationError('input value does not match the selected element.');
                                reject();
                            }
                            resolve();
                        }, 100)
                    });
                  },
                  allowOutsideClick: () => !swal.isLoading()
                }).then((result) => {
                  if (result) {
                    callback();
                  }
                }).catch((result) => {});
            };

            deleteSwal(deleteImportantInformationFunction);
        },

        authorizeUserSwal: authorizeUserSwal
    }
  });
