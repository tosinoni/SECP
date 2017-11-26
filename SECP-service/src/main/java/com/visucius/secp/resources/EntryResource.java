package com.visucius.secp.resources;

import com.visucius.secp.DTO.LoginRequestDTO;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.UseCase.LoginRequestController;
import com.visucius.secp.UseCase.UserRegistrationController;
import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.models.User;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class EntryResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntryResource.class);

    private final UserRegistrationController userRegistrationController;
    private final LoginRequestController loginRequestController;

    public EntryResource(UserRegistrationController userRegistrationController, LoginRequestController loginRequestController) {

        this.userRegistrationController = userRegistrationController;
        this.loginRequestController = loginRequestController;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    @Path("/register")
    public Response create(UserRegistrationRequest request) {

        UserRegistrationResponse response = userRegistrationController.handle(request);

        if (response.success) {
            return Response.status(response.status).entity(response.toString()).build();
        }

        throw new WebApplicationException(response.getErrors(), response.status);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/login")
    public Response login(LoginRequestDTO loginRequestDTO) {
        return loginRequestController.login(loginRequestDTO);
    }

    @GET
    @Path("/auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response verifyUser(@PathParam("username") String username) {
        User user = userRegistrationController.findUserByUsername(username);

        if(user == null) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
