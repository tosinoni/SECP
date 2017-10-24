package com.visucius.secp.Resource;



import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.UseCase.UserRegistrationController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/register")
public class RegisterResource {

    private final UserRegistrationController userRegistrationController;

    public RegisterResource(UserRegistrationController userRegistrationController)
    {
        this.userRegistrationController = userRegistrationController;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(UserRegistrationRequest request) {
        UserRegistrationResponse response = userRegistrationController.handle(request);

        if (response.Success) {
            return Response.status(response.status).entity(response.Message).build();
        }

        throw new WebApplicationException(response.Message, response.status);
    }
}
