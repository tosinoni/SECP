package com.visucius.secp.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

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
        return !StringUtils.isEmpty(name) && name.length() < 20 && name.length() > 2 ;
    }

    public static boolean isEmailValid(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean isAvatarURLValid(String avatarURL){return UrlValidator.getInstance().isValid(avatarURL); }
}
