package com.visucius.secp.util;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Pattern;

public class InputValidator {

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";


    public static boolean isPasswordValid(String password)
    {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }

    public static boolean isNameValid(String name)
    {
        return name != null && name.length() > 2 && name.length() < 20;
    }

    public static boolean isEmailValid(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }

}
