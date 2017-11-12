package com.visucius.secp.UseCase;

public interface ErrMsg {
    //LoginRequestDTO error messages
    String LOGIN_FAIL_NO_CREDENTIALS = "Login Failed. Please provide your username and password.";
    String LOGIN_FAIL_NO_USERNAME = "Login Failed. Please provide your username.";
    String LOGIN_FAIL_NO_PASSWORD = "Login Failed. Please provide your password.";
    String LOGIN_FAIL_USER_NOT_FOUND = "Login Failed. username does not exist.";
    String LOGIN_FAIL_WRONG_PASSWORD = "Login Failed. Incorrect password.";
}
