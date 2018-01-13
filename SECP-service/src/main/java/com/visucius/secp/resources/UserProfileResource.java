package com.visucius.secp.resources;

import com.visucius.secp.Controllers.User.UserProfileController;
import com.visucius.secp.DTO.UserDTO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.validator.constraints.URL;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/user/profile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserProfileResource {
    private final UserProfileController userProfileController;

    public UserProfileResource(UserProfileController userProfileController) {this.userProfileController = userProfileController;}

    @GET
    @Path("/id/{id}")
    @UnitOfWork
    public Response getProfile(@PathParam("id") String id){
        return userProfileController.getProfile(id);
    }

    @POST
    @Path("/modify")
    @UnitOfWork
    public Response modifyProfile(@Auth UserDTO userDTO){
        return userProfileController.modifyProfile(userDTO);
    }

    /*
    @POST
    @Path("/id/{id}/displayname")
    @UnitOfWork
    public Response addDisplayName(@Auth UserDTO userDTO, @PathParam("id") long id){
        return userProfileController.setDisplayName(userDTO,id);
    }

    @GET
    @Path("/id/{id}/displayname")
    @UnitOfWork
    public Response getDisplayName(@PathParam("id") String id) {
        return userProfileController.getDisplayName(id);
    }

    @GET
    @Path("/id/{id}/avatar_url")
    @UnitOfWork
    @URL
    public Response getAvatarURL(@PathParam("id") String id){
        return userProfileController.getAvatarURL(id);
    }

    @POST
    @Path("/id/{id}/avatar_url")
    @UnitOfWork
    @URL
    public Response addAvatarURL(@Auth UserDTO userDTO, @PathParam("id") long id){
        return userProfileController.setAvatarURL(userDTO, id);
    }
*/

}
