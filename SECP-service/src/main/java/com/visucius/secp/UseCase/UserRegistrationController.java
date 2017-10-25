package com.visucius.secp.UseCase;

import com.visucius.secp.Contracts.IRequestHandler;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.util.InputValidator;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class UserRegistrationController implements IRequestHandler<UserRegistrationRequest, UserRegistrationResponse> {


    public static final String FIRST_NAME_INVALID = "First name is not valid.";
    public static final String LAST_NAME_INVALID = "Last name is not valid.";
    public static final String EMAIL_INVALID = "Email is not valid.";
    public static final String PASSWORD_INVALID = "Password is not valid";
    public static final String USER_CREATED = "User Created";
    public static final String USER_NOT_CREATED = "User not created";

    public UserRegistrationController()
    {
    }

    @Override
    public UserRegistrationResponse handle(UserRegistrationRequest userRegistrationRequest) {

        List<String> errors = new ArrayList<>();

        if(!InputValidator.isNameValid(userRegistrationRequest.firstName))
        {
            errors.add(FIRST_NAME_INVALID);
        }

        if(!InputValidator.isNameValid(userRegistrationRequest.lastName))
        {
            errors.add(LAST_NAME_INVALID);

        }

        if(!InputValidator.isEmailValid(userRegistrationRequest.email))
        {
            errors.add(EMAIL_INVALID);

        }

        if(!InputValidator.isPasswordValid(userRegistrationRequest.password))
        {
            errors.add(PASSWORD_INVALID);
        }

        if(errors.size() > 0)
            return new UserRegistrationResponse(false,USER_NOT_CREATED, Response.Status.BAD_REQUEST,errors);

        //@TODO Store user

        return new UserRegistrationResponse(true, USER_CREATED, Response.Status.CREATED,errors);
    }
}

