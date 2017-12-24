package com.visucius.secp.Controllers.Admin;

public interface AdminErrorMessage {
    /*****Register ADMIN Error messages ******/
    String REGISTER_ADMIN_FAIL_INVALID_USER = "Admin Registration Failed. User does not Exist.";
    String REGISTER_ADMIN_FAIL_DUPLICATE_ENTRY = "Admin Registration Failed. User is already an admin.";

    /*****DELETE ADMIN Error messages ******/
    String DELETE_ADMIN_FAIL_INVALID_USER = "Deleting Admin Failed. User is not an admin.";
}
