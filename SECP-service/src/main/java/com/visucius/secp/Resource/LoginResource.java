package com.visucius.secp.Resource;


import com.visucius.secp.DTO.UserLoginRequest;
import com.visucius.secp.DTO.UserLoginResponse;
import com.visucius.secp.UseCase.UserLoginController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/login")
public class LoginResource {

    private final UserLoginController loginController;

    public LoginResource(UserLoginController loginController)
    {
        this.loginController = loginController;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(UserLoginRequest userLoginRequest)
    {
        UserLoginResponse userLoginResponse = this.loginController.handle(userLoginRequest);

        if(userLoginResponse.success) {
            Response.status(userLoginResponse.status)
                .entity(userLoginResponse.message + userLoginResponse.accessToken).build();
        }

        throw new WebApplicationException(userLoginResponse.message, userLoginResponse.status);

    }
}
