package com.visucius.secp.resources;

import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.Controllers.User.UserController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.DTO.DeviceDTO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@RolesAllowed({"NORMAL", "ADMIN"})
public class UserResource {
    private final UserController userController;
    private final UserRegistrationController userRegistrationController;


    public UserResource(UserController userController, UserRegistrationController userRegistrationController) {
        this.userController = userController;
        this.userRegistrationController = userRegistrationController;
    }

    @GET
    @Path("/verify/admin/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response isUserAnAdmin(@Auth @PathParam("id") String id) {

        if(!StringUtils.isBlank(id) && userController.isUserAnAdmin(id)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/verify/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response verifyUsername(@Auth @PathParam("username") String username) {

        if(!StringUtils.isBlank(username) && userRegistrationController.isUsernameValid(username)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/verify/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response verifyEmailAddress(@Auth @PathParam("email") String email) {

        if(!StringUtils.isBlank(email) && userRegistrationController.isEmailValid(email)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/device")
    public Response addDevice(@Auth DeviceDTO deviceDTO) {
        return userController.addDevice(deviceDTO);
    }

    @GET
    @Path("/id/{id}/device/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response verifyDevice(@Auth @PathParam("name") String deviceName, @PathParam("id") long id) {

        if(!StringUtils.isBlank(deviceName) && userController.isDeviceRegisteredForUser(id, deviceName)) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/id/{id}/publicKey")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response getUsersPublicKeys(@Auth @PathParam("id") long id) {
        return userController.getUsersPublicKeys(id);
    }

    @GET
    @UnitOfWork
    public Response getAllUsers() {
        return userController.getAllUsers();
    }

    @GET
    @Path("/id/{id}")
    @UnitOfWork
    public Response getUser(@Auth @PathParam("id") String id) {
        return userController.getUserGivenId(id);
    }
}
