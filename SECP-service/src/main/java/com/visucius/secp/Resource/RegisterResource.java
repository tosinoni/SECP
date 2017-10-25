package com.visucius.secp.Resource;



import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.UseCase.UserRegistrationController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/register")
public class RegisterResource {

    private final UserRegistrationController userRegistrationController;

    public RegisterResource(UserRegistrationController userRegistrationController)
    {
        this.userRegistrationController = userRegistrationController;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        UserRegistrationResponse response = userRegistrationController.handle(request);

        if (response.success) {
            return response;
        }

        throw new WebApplicationException(response.getErrors(), response.status);
    }
}
