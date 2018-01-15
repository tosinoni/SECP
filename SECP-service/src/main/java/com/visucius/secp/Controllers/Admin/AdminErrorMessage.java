package com.visucius.secp.Controllers.Admin;

public interface AdminErrorMessage {
    /*****General ADMIN Error messages ******/
    String INVALID_ID = "Operation Failed. Invalid %s id provided.";
    String DOES_NOT_EXIST = "Operation Failed. %s does not Exist.";

    /*****Register ADMIN Error messages ******/
    String REGISTER_ADMIN_FAIL_DUPLICATE_ENTRY = "Admin Registration Failed. User is already an admin.";

    /*****DELETE ADMIN Error messages ******/
    String DELETE_ADMIN_FAIL_INVALID_USER = "Deleting Admin Failed. User is not an admin.";

    /*****Roles Error messages ******/
    String REGISTER_ROLES_FAIL_EMPTY_REQUEST = "Registering roles failed. No role provided.";
    String REGISTER_ROLES_FAIL_NAME_EXISTS = "Registering roles failed. Role %s already exists.";
    String REGISTER_ROLES_FAIL_INVALID_COLOR = "Registering roles failed. No color provided.";
    String DELETE_ROLES_FAIL_ROLE_IN_USE = "Delete role %s failed. There are groups or users using this role.";


    /*****Permissions Error messages ******/
    String REGISTER_PERMISSIONS_FAIL_EMPTY_REQUEST = "Registering permissions failed. No permission provided.";
    String REGISTER_PERMISSIONS_FAIL_NAME_EXISTS = "Registering permissions failed. Permission %s already exists.";
    String REGISTER_PERMISSIONS_FAIL_INVALID_COLOR = "Registering permissions failed. No color provided.";
    String DELETE_PERMISSION_FAIL_PERMISSION_IN_USE = "Delete permission %s failed. There are groups or users using this permission.";

}
