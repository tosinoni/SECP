package com.visucius.secp.resources;


import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.Controllers.GroupController;
import com.visucius.secp.DTO.GroupCreationRequest;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    @Path("/groups")
    public Response create(@Auth GroupCreationRequest request) {
        return groupController.createGroup(request);
    }
}
