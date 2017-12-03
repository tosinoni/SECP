angular.module('SECP')
  .controller('PortalController', ['$scope', function ($scope) {
      $('.audit-user-select').select2();
      $scope.validateUserAudit = function() {
          var user = document.forms["useraudit"]["username"].value;
          if (user == "") {
              swal({
                  position: 'top-right',
                  type: 'error',
                  title: 'Please specify a username',
                  showConfirmButton: false,
                  timer: 1500
              });
          }
      }
  }]);
