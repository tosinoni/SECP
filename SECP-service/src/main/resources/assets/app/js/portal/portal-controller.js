angular.module('SECP')
  .controller('PortalController', ['$scope', function ($scope) {
      //Beautifies select field to searchable dropdowns
      $('.audit-user-select').select2();
      //Ensures the admin is not able to audit without specifying a user
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
      //Populates the advanced search time for user auditing
      $('input[name=todate]').val(moment().format('YYYY-MM-DDTHH:mm'))
  }]);
angular.module('SECP').controller('UserAuditSearch', function ($scope) {
    $scope.isCollapsed = true;
});
