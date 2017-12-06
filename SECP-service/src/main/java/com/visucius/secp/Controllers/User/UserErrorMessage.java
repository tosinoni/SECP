package com.visucius.secp.Controllers.User;

public interface UserErrorMessage {
    //LoginRequestDTO error messages
    String LOGIN_FAIL_NO_CREDENTIALS = "Login Failed. Please provide your username and password.";
    String LOGIN_FAIL_NO_USERNAME = "Login Failed. Please provide your username.";
    String LOGIN_FAIL_NO_PASSWORD = "Login Failed. Please provide your password.";
    String LOGIN_FAIL_USER_NOT_FOUND = "Login Failed. username does not exist.";
    String LOGIN_FAIL_WRONG_PASSWORD = "Login Failed. Incorrect password.";

    //Register error messages
    String FIRST_NAME_INVALID = "First name is not valid.";
    String LAST_NAME_INVALID = "Last name is not valid.";
    String User_NAME_INVALID = "Username is not valid.";
    String EMAIL_INVALID = "Email is not valid.";
    String PASSWORD_INVALID = "Password is not valid";
    String USER_CREATED = "User Created";
    String USER_NOT_CREATED = "User not created";
    String DUPLICATE_USERNAME = "Username already exists";
    String DUPLICATE_EMAIL = "Email is in use";
}
