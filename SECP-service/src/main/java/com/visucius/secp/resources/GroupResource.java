package com.visucius.secp.resources;


import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.Controllers.GroupController;
import com.visucius.secp.DTO.GroupCreateRequest;
import com.visucius.secp.DTO.GroupModifyRequest;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("/groups")
//@RolesAllowed("ADMIN")
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
    public Response create( GroupCreateRequest request) {
        return groupController.createGroup(request);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    @Path("/modify/{id}")
    public Response modifyRoles(GroupModifyRequest request, @PathParam("id") int id)
    {
        return  groupController.modifyGroup(request,id);
    }

}
