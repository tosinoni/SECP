'use strict';

angular.module('SECP')
  .factory('Admin', function($http) {
    return {
        addRoles : function(request) {
            return $http.post("/SECP/admin/roles", request)
            .then(function(res) {
                 return res;
            }, function(err) {
                return err;
            });
        },

        addPermissions : function(request) {
            return $http.post("/SECP/admin/permissions", request)
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

        editGroup : function(data) {
            //this needs to be changed on the server side.
            return $http.post("/SECP/groups/modify",data)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        addGroup : function(data) {
            //this needs to be changed on the server side
            return $http.post("/SECP/groups/", data)
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
            return $http.post("/SECP/user/modify/", data)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        deleteGroup : function(id) {
            return $http.delete("/SECP/groups/" + id)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        deleteUser : function(id) {
            return $http.delete("/SECP/user/id/" + id)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        getFilter : function(id) {
            return $http.get("/SECP/filter/id/" + id)
                .then(function(res) {
                    if (res.status == 200) {
                        return res.data;
                    }
                }, function(err) {
                    return err;
                });
        },

        getAllFilters : function() {
            return $http.get("/SECP/filter")
                .then(function(res) {
                    if (res.status == 200) {
                        return res.data;
                    }
                }, function(err) {
                    return err;
                });
        },

        deleteFilter : function(id) {
            return $http.delete("/SECP/filter/" + id)
                .then(function(res) {
                    return res;
                }, function(err) {
                    return err;
                });
        },

        addFilter : function(request) {
            return $http.post("/SECP/filter", request)
                .then(function(res) {
                    return res;
                }, function(err) {
                    return err;
                });
        },

        editFilter : function(data) {
            return $http.post("/SECP/filter/modify",data)
                .then(function(res) {
                    return res;
                }, function(err) {
                    return err;
                });
        },

        modifyRole : function(data) {
            return $http.post("/SECP/admin/roles/id/" + data.id, data)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        modifyPermission : function(data) {
            return $http.post("/SECP/admin/permissions/id/" + data.id, data)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        auditUser : function(request) {
            return $http.post("/SECP/admin/audit/user", request)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        },

        auditGroup : function(request) {
            return $http.post("/SECP/admin/audit/groups", request)
            .then(function(res) {
                return res;
            }, function(err) {
                return err;
            });
        }
    }
  });
