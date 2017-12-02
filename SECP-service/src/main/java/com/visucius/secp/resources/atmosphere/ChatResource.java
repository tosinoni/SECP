package com.visucius.secp.resources.atmosphere;

import com.visucius.secp.DTO.MessageResponseDTO;
import com.visucius.secp.util.ChatEncoder;
import org.atmosphere.config.service.*;
import org.atmosphere.cpr.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import java.io.IOException;

@ManagedService(path = "/{GroupID}")
public class ChatResource {
    private final Logger logger = LoggerFactory.getLogger(ChatResource.class);

    @PathParam("GroupID")
    private long GroupID;

    @Inject
    private BroadcasterFactory factory;


    @Inject
    private AtmosphereResourceFactory resourceFactory;


    @Inject
    private MetaBroadcaster metaBroadcaster;


    @Ready
    public void onReady(final AtmosphereResource r) {
        logger.info("Browser {} connected.", r.uuid());
        logger.info("BroadcasterFactory used {}", factory.getClass().getName());
    }

    @Disconnect
    public void onDisconnect(AtmosphereResourceEvent event) {
        if (event.isCancelled()) {
            logger.info("Browser {} unexpectedly disconnected", event.getResource().uuid());
        } else if (event.isClosedByClient()) {
            logger.info("Browser {} closed the connection", event.getResource().uuid());
        }
    }

    /* @Message(encoders = {JacksonEncoder.class}, decoders = {JacksonDecoder.class})
    public Data onMessage(Data message) throws IOException {
        logger.info("{} just send {}",message.getAuthor(), message.getMessage());
        return message;
    }*/

    @org.atmosphere.config.service.Message(encoders = {ChatEncoder.class}, decoders = {ChatEncoder.class})
    public void onPrivateMessage(MessageResponseDTO message, AtmosphereResource r) throws IOException {
        System.out.println(r.uuid()+"______________");
        // Retrieve the original AtmosphereResource
        System.out.println("In private notification+++++++++================");
        factory.lookup("/chat/*").broadcast(message.getAuthor() +"Someone Pinged you", r);

    }

//    @Broadcast(writeEntity = false)
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response broadcast(Message message) {
//        MessageResponseDTO messageResponseDTO = new MessageResponseDTO(message.getUser().getUsername(), message.getBody());
//
//        return Response.ok().entity(messageResponseDTO).build();
//    }
}
