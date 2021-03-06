package com.visucius.secp.Controllers.User;

public interface UserErrorMessage {
    //Default messages
    String USER_ID_INVALID = "Operation failed. User id is not valid.";

    //LoginRequestDTO error messages
    String LOGIN_FAIL_NO_CREDENTIALS = "Login Failed. Please provide your username and password.";
    String LOGIN_FAIL_NO_USERNAME = "Login Failed. Please provide your username.";
    String LOGIN_FAIL_NO_PASSWORD = "Login Failed. Please provide your password.";
    String LOGIN_FAIL_USER_NOT_FOUND = "Login Failed. username does not exist.";
    String LOGIN_FAIL_WRONG_PASSWORD = "Login Failed. Incorrect password.";
    String LOGIN_USER_IS_NOT_ACTIVE = "Login Failed. Please contact admin.";

    //Register error messages
    String FIRST_NAME_INVALID = "First name is not valid.";
    String LAST_NAME_INVALID = "Last name is not valid.";
    String User_NAME_INVALID = "Username is not valid.";
    String DISPLAY_NAME_INVALID = "Display name is not valid.";
    String EMAIL_INVALID = "Email is not valid.";
    String PASSWORD_INVALID = "Password is not valid";
    String USER_CREATED = "User Created";
    String USER_NOT_CREATED = "User not created";
    String DUPLICATE_USERNAME = "Username already exists";
    String DUPLICATE_EMAIL = "Email is in use";

    //Device error messages
    String DEVICE_ADD_FAIL_NO_DEVICE_INFO = "Device addition Failed. Please provide the device name and public key.";
    String DEVICE_ADD_FAIL_NO_DEVICE_NAME = "Device addition Failed. Please provide the device name.";
    String DEVICE_ADD_FAIL_INVALID_PUBLIC_KEY = "Device addition Failed. Please provide a valid public key for device.";
    String DEVICE_ADD_FAIL_USER_NOT_FOUND = "Device addition Failed. User id does not exist.";
    String DEVICE_ADD_FAIL_DEVICE_EXISTS = "Device addition Failed. Device already exist for the user";

    //secret error messages
    String SECRET_ADD_FAIL_NO_SECRET_INFO = "Encryption failed. Encryption key was not provided.";
    String SECRET_ADD_FAIL_INVALID_DEVICE = "Encryption failed. Invalid device provided.";
    String SECRET_ADD_FAIL_INVALID_GROUP = "Encryption failed. Invalid group provided.";
    String SECRET_ADD_FAIL_NO_SECRET_KEY = "Encryption failed. Encryption key was not provided.";
    String SECRET_ADD_FAIL_NO_DEVICE = "Encryption failed. No device provided.";


    //Get devices error messages
    String DEVICE_GET_FAIL_NO_DEVICE_EXISTS = "No device exists for user.";
    String DEVICE_GET_FAIL_NO_PUBLIC_KEY_EXISTS = "No public key exist for all user's devices.";

    //UserProfile error messages
    String USER_PROFILE_FAILED_NO_USER_PROFILE = "Failed to find user profile. No profile exists.";
    String DISPLAY_NAME_FAILED_NO_DISPLAY_NAME = "Failed to set the display name. No display name chosen.";
    String AVATAR_URL_FAILED_NO_AVATAR_URL = "Failed to set the Avatar URL. No Avatar chosen.";

    //modify user error messages
    String MODIFY_USER_FAIL_NO_USER_INFO = "modify user failed. Please provide the information for the user.";
    String MODIFY_USER_FAIL_PERMISSIONS_REQUIRED = "modify user failed. A user must have only one permission";
    String MODIFY_USER_FAIL_PERMISSIONS_INVALID = "modify user failed. Permission %s does not exist";
    String MODIFY_USER_FAIL_ROLES_INVALID = "modify user failed. role %s does not exist";
}
