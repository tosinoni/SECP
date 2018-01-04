'use strict';

angular.module('SECP')
  .factory('Admin', function($http) {
    return {
        addRoles : function(setOfRoles) {
            var req = {roles: setOfRoles}
            return $http.post("/SECP/admin/roles", req)
            .then(function(res) {
                 return res;
            }, function(err) {
                return err;
            });
        },

        addPermissions : function(setOfPermissions) {
            var req = {permissions: setOfPermissions}
            return $http.post("/SECP/admin/permissions", req)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        getPermissions : function() {
            return $http.get("/SECP/admin/permissions")
            .then(function(res) {
                 if (res.status == 200) {
                    return res.data;
                 }
            }, function(err) {
                return err;
            });
        },

        getRoles : function() {
            return $http.get("/SECP/admin/roles")
            .then(function(res) {
                 if (res.status == 200) {
                    return res.data;
                 }
            }, function(err) {
                return err;
            });
        },

        deleteRole : function(id) {
            return $http.delete("/SECP/admin/role/id/" + id)
            .then(function(res) {
                 return res;
            }, function(err) {
                return err;
            });
        },

        deletePermission : function(id) {
            return $http.delete("/SECP/admin/permission/id/" + id)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },
    }
  });
