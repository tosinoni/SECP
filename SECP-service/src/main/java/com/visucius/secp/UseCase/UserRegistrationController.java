package com.visucius.secp.UseCase;

import com.visucius.secp.Contracts.IRequestHandler;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.util.InputValidator;

import javax.ws.rs.core.Response;

public class UserRegistrationController implements IRequestHandler<UserRegistrationRequest, UserRegistrationResponse> {


    public static final String FIRST_NAME_INVALID = "First name is not valid.";
    public static final String LAST_NAME_INVALID = "Last name is not valid.";
    public static final String EMAIL_INVALID = "Email is not valid.";
    public static final String PASSWORD_INVALID = "Password is not valid";
    public static final String USER_CREATED = "User Created";

    public UserRegistrationController()
    {
    }

    @Override
    public UserRegistrationResponse handle(UserRegistrationRequest userRegistrationRequest) {

        if(!InputValidator.isNameValid(userRegistrationRequest.firstName))
        {
            return new UserRegistrationResponse(false,FIRST_NAME_INVALID, Response.Status.BAD_REQUEST);
        }

        if(!InputValidator.isNameValid(userRegistrationRequest.lastName))
        {
            return new UserRegistrationResponse(false,LAST_NAME_INVALID, Response.Status.BAD_REQUEST);

        }

        if(!InputValidator.isEmailValid(userRegistrationRequest.email))
        {
            return new UserRegistrationResponse(false,EMAIL_INVALID, Response.Status.BAD_REQUEST);

        }

        if(!InputValidator.isPasswordValid(userRegistrationRequest.password))
        {
            return new UserRegistrationResponse(false,PASSWORD_INVALID, Response.Status.BAD_REQUEST);
        }


        return new UserRegistrationResponse(true, USER_CREATED, Response.Status.CREATED);
    }
}

