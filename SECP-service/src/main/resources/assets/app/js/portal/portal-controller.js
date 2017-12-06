angular.module('SECP')
  .controller('PortalController', ['$scope', function ($scope) {
      //Beautifies select field to searchable dropdowns
      $('.audit-user-select').select2();
      $('.audit-group-select').select2();
      //Ensures the admin is not able to audit without specifying a user
      $scope.validateUserAudit = function() {
          if (!$scope.auditUser) {
              swal({
                  position: 'top-right',
                  type: 'error',
                  title: 'Please specify a username',
                  showConfirmButton: false,
                  timer: 1500
              });
          }
      }
      //Ensures the admin is not able to audit without specifying a group
      $scope.validateGroupAudit = function() {
          if (!$scope.auditGroup) {
              swal({
                  position: 'top-right',
                  type: 'error',
                  title: 'Please specify a group to audit',
                  showConfirmButton: false,
                  timer: 1500
              });
          }
      }
      //Populates the advanced search time for user auditing
      $('input[name=todate]').val(moment().format('YYYY-MM-DDTHH:mm'))
  }]);
//Collapsible for User Audit Advanced Search
angular.module('SECP').controller('UserAuditAdvancedSearch', function ($scope) {
    $scope.isCollapsed = true;
});
//Collapsible for Group Audit Advanced Search
angular.module('SECP').controller('GroupAuditAdvancedSearch', function ($scope) {
    $scope.isCollapsed = true;
});
