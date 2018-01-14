package com.visucius.secp.Controllers;

import com.visucius.secp.DTO.MessageDTO;
import com.visucius.secp.daos.MessageDAO;
import com.visucius.secp.models.Message;
import com.visucius.secp.models.User;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;


public class MessageController {

    private MessageDAO messageRepository;

    public static final int MAX_LIMIT = 50;
    public static final int DEFAULT_LIMIT = 20;

    public MessageController(MessageDAO messageRepository)
    {
        this.messageRepository = messageRepository;
    }


    public Response getMessagesForGroup(long groupID, int offset, int limit)
    {
        if(offset < 0)
            offset = 0;
        if(limit <= 0)
            limit = DEFAULT_LIMIT;
        else if(limit > MAX_LIMIT)
            limit = MAX_LIMIT;

        List<Message> messages = messageRepository.findMessageWithGroupID(groupID,offset,limit);
        List<MessageDTO> messageDTOS = messages.stream().map(
            (message) ->
            {
                return new MessageDTO(
                    message.getId(),
                    groupID,
                    message.getUser().getId(),
                    message.getBody(),
                    MessageDTO.MessageType.MESSAGE,
                    message.getTimestamp());

            })
            .collect(Collectors.toList());

        return Response.status(Response.Status.OK).
            entity(
                messageDTOS).build();
    }
}
