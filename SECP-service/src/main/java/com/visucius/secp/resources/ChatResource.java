package com.visucius.secp.resources;

import com.visucius.secp.Controllers.chat.ChatController;
import com.visucius.secp.models.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/chats")
@RolesAllowed({"ADMIN", "NORMAL"})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChatResource {

    private ChatController chatController;

    public ChatResource(ChatController chatController) {
        this.chatController = chatController;
    }

    @GET
    @Path("search/{value}")
    @UnitOfWork
    public Response search(@Auth User user, @PathParam("value") String value) {
        return chatController.search(user, value);
    }
}
