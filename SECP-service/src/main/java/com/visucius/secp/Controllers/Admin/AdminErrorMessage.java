package com.visucius.secp.Controllers.Admin;

public interface AdminErrorMessage {
    /*****General ADMIN Error messages ******/
    String INVALID_USER_ID = "Operation Failed. Invalid user id provided.";
    String USER_DOES_NOT_EXIST = "Operation Failed. User does not Exist.";

    /*****Register ADMIN Error messages ******/
    String REGISTER_ADMIN_FAIL_DUPLICATE_ENTRY = "Admin Registration Failed. User is already an admin.";

    /*****DELETE ADMIN Error messages ******/
    String DELETE_ADMIN_FAIL_INVALID_USER = "Deleting Admin Failed. User is not an admin.";
}
