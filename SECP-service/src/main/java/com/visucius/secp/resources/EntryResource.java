package com.visucius.secp.resources;

import com.visucius.secp.Controllers.User.UserController;
import com.visucius.secp.DTO.LoginRequestDTO;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.Controllers.User.LoginRequestController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;
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
    private final UserController userController;

    public EntryResource(UserRegistrationController userRegistrationController,
                         LoginRequestController loginRequestController,
                         UserController userController) {

        this.userRegistrationController = userRegistrationController;
        this.loginRequestController = loginRequestController;
        this.userController = userController;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    @Path("/register")
    public Response create(@Auth UserRegistrationRequest request) {

        UserRegistrationResponse response = userRegistrationController.registerUser(request);

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
    @Path("/user/verify/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response verifyEmailAddress(@PathParam("email") String email) {

        if(!StringUtils.isBlank(email) && userRegistrationController.isEmailValid(email)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/user/verify/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response verifyUsername(@PathParam("username") String username) {

        if(!StringUtils.isBlank(username) && userRegistrationController.isUsernameValid(username)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/user/verify/admin/{id}")
    @Timed
    @UnitOfWork
    public Response isUserAnAdmin(@Auth @PathParam("id") String id) {

        if(!StringUtils.isBlank(id) && userController.isUserAnAdmin(id)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
