package com.visucius.secp.UseCase;


import com.visucius.secp.Contracts.IPasswordRepository;
import com.visucius.secp.Contracts.IRequestHandler;
import com.visucius.secp.Contracts.IUserRepository;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;

import javax.ws.rs.core.Response;

public class UserRegistrationController implements IRequestHandler<UserRegistrationRequest, UserRegistrationResponse> {

    private final IPasswordRepository passwordRepository;
    private final IUserRepository userRepository;

    public UserRegistrationController(IPasswordRepository passwordRepository, IUserRepository userRepository)
    {
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }

    @Override
    public UserRegistrationResponse handle(UserRegistrationRequest userRegistrationRequest) {
        //@TODO implement user registration
        return new UserRegistrationResponse(true, "User Created", Response.Status.CREATED);
    }
}

