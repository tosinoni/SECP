package com.visucius.secp.UseCase;

import com.visucius.secp.Contracts.IRequestHandler;
import com.visucius.secp.DTO.UserLoginRequest;
import com.visucius.secp.DTO.UserLoginResponse;

import javax.ws.rs.core.Response;

public class UserLoginController implements IRequestHandler<UserLoginRequest, UserLoginResponse> {


    public UserLoginController()
    {
    }

    @Override
    public UserLoginResponse handle(UserLoginRequest userLoginRequest) {
        // @TODO implement user login

        return new UserLoginResponse(true,"Login Successful", Response.Status.OK,"");
    }
}
