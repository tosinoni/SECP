angular.module('SECP')
  .controller('PortalController', function ($scope, Admin, $location) {
      $scope.users = [];
      $scope.fromUsers = [];
      $scope.toUsersList = [];

      Admin.getAllUsers().then(function (users) {
          $scope.fromUsers = angular.copy(users);
          $scope.toUsersList = angular.copy(users);
          $scope.users = angular.copy(users);
      });

      $scope.$watch('audit.fromUser', function(newFromAuditUser) {
          if(newFromAuditUser) {
              let index = _.findIndex($scope.users, function(u) { return u.userID == newFromAuditUser.userID; });
              let newToUsers = angular.copy($scope.users);
              newToUsers.splice(index, 1);
              $scope.toUsersList = newToUsers;
          }
      });


      //Ensures the admin is not able to audit without specifying a user
      $scope.validateUserAudit = function() {
          if (!$scope.audit || !$scope.audit.fromUser) {
              swal({
                  position: 'top-right',
                  type: 'error',
                  title: 'Please specify a user to audit',
                  showConfirmButton: false,
                  timer: 1500
              }).catch((result) => {});
          } else {
              Admin.auditUser($scope.audit).then(function (res) {
                  if(res.status !== 200) {
                      swal("Audit request failed", res.data.message, "error");
                  } else {
                      let user = $scope.audit.fromUser;
                      let userName = user.firstName + ' ' + user.lastName;
                      $location.path('/portal/audit/view').search({
                          groups: res.data,
                          name: userName,
                          auditUser: user
                      });
                      swal("Yaah", "Audit request successful", "success");
                  }
              })
          }
      }

      //Ensures the admin is not able to audit without specifying a group
      $scope.validateGroupAudit = function() {
          if (!$scope.audit || !scope.audit.fromUser) {
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
      $('input[name=todate]').val(moment().format('YYYY-MM-DDTHH:mm'));
  });
//Collapsible for User Audit Advanced Search
angular.module('SECP').controller('UserAuditAdvancedSearch', function ($scope) {
    $scope.isCollapsed = true;
});
//Collapsible for Group Audit Advanced Search
angular.module('SECP').controller('GroupAuditAdvancedSearch', function ($scope) {
    $scope.isCollapsed = true;
});
