package com.visucius.secp.resources;


import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.Controllers.GroupController;
import com.visucius.secp.DTO.GroupCreateRequest;
import com.visucius.secp.DTO.GroupDTO;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.models.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class GroupResource {

    private GroupController groupController;

    public GroupResource(GroupController controller)
    {
        this.groupController = controller;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public Response createPublicGroup(@Auth GroupCreateRequest request) {
        return groupController.createPublicGroup(request);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    @RolesAllowed({"ADMIN","NORMAL"})
    @Path("/private")
    public Response createPrivateGroup(@Auth User user, UserDTO userDTO) {
        return groupController.createPrivateGroup(user, userDTO);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @RolesAllowed({"ADMIN", "NORMAL"})
    @Path("/user")
    public Response getGroupForUser(@Auth User user)
    {
        return groupController.getGroupsForUser(user);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    @Path("/modify")
    public Response modifyGroup(@Auth GroupDTO request)
    {
        return  groupController.modifyGroup(request);
    }

    @DELETE
    @UnitOfWork
    @Path("/{id}")
    public Response deleteGroup(@Auth @PathParam("id") int id)
    {
        return  groupController.deleteGroup(id);
    }

    @GET
    @UnitOfWork
    public Response getAllGroups() {
        return groupController.getAllGroups();
    }

}
