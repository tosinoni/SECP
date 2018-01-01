package com.visucius.secp.resources;

import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.Controllers.MessageController;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/messages")
@RolesAllowed({"NORMAL", "ADMIN"})
public class MessageResource {

    private final MessageController messageController;

    public MessageResource(MessageController messageController)
    {
        this.messageController = messageController;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    @Path("/group/{groupID}")
    public Response getMessagesForGroup(
        @Auth @PathParam("groupID") long id,
        @QueryParam("offset") int offset,
        @QueryParam("limit") int limit)
    {
        return messageController.getMessagesForGroup(id,offset,limit);
    }

}
