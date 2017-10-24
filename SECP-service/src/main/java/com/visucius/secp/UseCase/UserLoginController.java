package com.visucius.secp.UseCase;

import com.visucius.secp.Contracts.IPasswordRepository;
import com.visucius.secp.Contracts.IRequestHandler;
import com.visucius.secp.Contracts.IUserRepository;
import com.visucius.secp.DTO.UserLoginRequest;
import com.visucius.secp.DTO.UserLoginResponse;

import javax.ws.rs.core.Response;

public class UserLoginController implements IRequestHandler<UserLoginRequest, UserLoginResponse> {

    private final IUserRepository userRepository;
    private final IPasswordRepository passwordRepository;

    public UserLoginController(IUserRepository userRepository, IPasswordRepository passwordRepository)
    {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }

    @Override
    public UserLoginResponse handle(UserLoginRequest userLoginRequest) {
        // @TODO implement user login
        return new UserLoginResponse(true,"Login Successfull", Response.Status.OK,"");
    }
}
