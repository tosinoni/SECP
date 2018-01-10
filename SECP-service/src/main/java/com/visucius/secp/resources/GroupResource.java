package com.visucius.secp.resources;


import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.Controllers.GroupController;
import com.visucius.secp.DTO.GroupCreateRequest;
import com.visucius.secp.DTO.GroupDTO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("/groups")
@Consumes(MediaType.APPLICATION_JSON)
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
    public Response create(@Auth GroupCreateRequest request) {
        return groupController.createGroup(request);
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
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

    @GET
    @Path("/id/{id}")
    @UnitOfWork
    public Response getGroup(@Auth @PathParam("id") String id) {
        return groupController.getGroupGivenId(id);
    }
}
