package com.visucius.secp.UseCase;

import com.visucius.secp.Contracts.IRequestHandler;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.util.PasswordUtil;
import org.apache.commons.validator.routines.EmailValidator;

import javax.ws.rs.core.Response;

public class UserRegistrationController implements IRequestHandler<UserRegistrationRequest, UserRegistrationResponse> {


    public UserRegistrationController()
    {
    }

    @Override
    public UserRegistrationResponse handle(UserRegistrationRequest userRegistrationRequest) {

        if(!isNameValid(userRegistrationRequest.firstName))
        {
            return new UserRegistrationResponse(false,"First name is not valid.", Response.Status.BAD_REQUEST);
        }

        if(!isNameValid(userRegistrationRequest.lastName))
        {
            return new UserRegistrationResponse(false,"Last name is not valid.", Response.Status.BAD_REQUEST);

        }

        if(!isEmailValid(userRegistrationRequest.email))
        {
            return new UserRegistrationResponse(false,"Email is not valid.", Response.Status.BAD_REQUEST);

        }

        if(!PasswordUtil.isPasswordValid(userRegistrationRequest.password))
        {
            return new UserRegistrationResponse(false,"Password is not valid.", Response.Status.BAD_REQUEST);
        }


        return new UserRegistrationResponse(true, "User Created", Response.Status.CREATED);
    }


    private boolean isNameValid(String name)
    {
        return name.length() > 2 && name.length() < 20;
    }

    private boolean isEmailValid(String email)
    {
        return EmailValidator.getInstance().isValid(email);
    }


}

