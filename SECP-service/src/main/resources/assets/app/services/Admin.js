'use strict';

angular.module('SECP')
  .factory('Admin', function($http) {
    //below needs to be changed. This is just a hack to conform to what the server expects
    var convertData = function(data) {
        var newData = angular.copy(data);
        if(data) {
            newData = angular.copy(data);
            newData.roles = newData.roles.map(role => {return role.id});
            if(newData.permissions)
                newData.permissions = newData.permissions.map(permission => {return permission.id});
        }
        return newData;
    }
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

        getAllGroups : function() {
            return $http.get("/SECP/groups")
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

        deleteGroup : function(id) {
            return $http.delete("/SECP/admin/group/id/" + id)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        editGroup : function(data) {
            //this needs to be changed on the server side.
            return $http.post("/SECP/admin/group/id/" + data.groupID, convertData(data))
            .then(function(res) {
                if(res.status == 200) {
                    return res;
                }
            }, function(err) {
                return err;
            });
        },

        addGroup : function(data) {
            //this needs to be changed on the server side
            return $http.post("/SECP/groups/", convertData(data))
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        getGroup : function(id) {
            return $http.get("/SECP/groups/id/" + id)
            .then(function(res) {
                if (res.status == 200) {
                    return res.data;
                }
            }, function(err) {
                return err;
            });
        },

        register : function(user) {
            return $http.post("/SECP/admin/register", user)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        getUser : function(id) {
            return $http.get("/SECP/user/id/" + id)
                .then(function(res) {
                    if (res.status == 200) {
                        return res.data;
                    }
                }, function(err) {
                    return err;
                });
        },

        getAllUsers : function() {
            return $http.get("/SECP/user")
                .then(function(res) {
                    if (res.status == 200) {
                        return res.data;
                    }
                }, function(err) {
                    return err;
                });
        },

        editUser : function(data) {
            //this needs to be changed on the server side.
            return $http.post("/SECP/user/modify/", data)
                .then(function(res) {
                    return res;
                }, function(err) {
                    return err;
                });
        },

    }
  });
