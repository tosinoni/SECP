package com.visucius.secp.resources;

import com.visucius.secp.Controllers.User.UserProfileController;
import com.visucius.secp.DTO.UserDTO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.validator.constraints.URL;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserProfileResource {
    private final UserProfileController userProfileController;

    public UserProfileResource(UserProfileController userProfileController) {this.userProfileController = userProfileController;}

    @GET
    @Path("/profile/{id}")
    @UnitOfWork
    public Response getProfile(@PathParam("id") String id){
        userProfileController.getProfile(id);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/profile/{id}/displayname")
    @UnitOfWork
    public Response addDisplayName(@Auth UserDTO userDTO, @PathParam("id") long id){
        userProfileController.setDisplayName(userDTO, id);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/profile/{id}/displayname")
    @UnitOfWork
    public Response getDisplayName(@PathParam("id") long id) {
        userProfileController.getDisplayName(id);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/profile/{id}/avatar_url")
    @UnitOfWork
    @URL
    public Response getAvatarURL(@PathParam("id") long id){
        userProfileController.getAvatarURL(id);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/profile/{id}/avatar_url")
    @UnitOfWork
    @URL
    public Response addAvatarURL(@Auth UserDTO userDTO, @PathParam("id") long id){
        userProfileController.setAvatarURL(userDTO, id);
        return Response.status(Response.Status.OK).build();
    }

    /*
    @GET
    @Path("/{id}/groups")
    @UnitOfWork
    public Response getUserMessages(@PathParam("id") long id){
        userProfileController.getUserMessages(id);
        return Response.status(Response.Status.OK).build();
    }*/
}