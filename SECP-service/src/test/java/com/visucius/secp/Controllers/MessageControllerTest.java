package com.visucius.secp.Controllers;

import com.visucius.secp.DTO.MessageDTO;
import com.visucius.secp.daos.MessageDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Message;
import com.visucius.secp.models.User;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MessageControllerTest {

    private static MessageController controller;
    private static MessageDAO messageDAO;
    private static List<MessageDTO> messageDTOS;
    private static List<Message> messages;
    private static long GROUP_ID =1;
    private static long USER_ID = 1;
    private static long MESSAGE_ID = 1;
    private static Date TIME = new Date();
    private static int NUMBER_MESSAGES = 200;

    @BeforeClass
    public static void setup()
    {
        messageDAO = Mockito.mock(MessageDAO.class);
        controller = new MessageController(messageDAO);
        messageDTOS = createMessageDTOS();
        messages = createMessages();
    }

    private static List<MessageDTO> createMessageDTOS() {
        List<MessageDTO> messages = new ArrayList<>();
        String body = "message number ";
        for (int i = 0; i < NUMBER_MESSAGES; i++) {
            MessageDTO messageDTO = new MessageDTO(
                MESSAGE_ID,
                GROUP_ID,
                USER_ID,
                TIME,
                body + i);
            messages.add(messageDTO);
        }
        return messages;
    }

    private static List<Message> createMessages() {
        List<Message> messages = new ArrayList<>();
        String body = "message number ";
        for (int i = 0; i < NUMBER_MESSAGES; i++) {

            User user = new User();
            Group group = new Group();
            group.setId(GROUP_ID);
            user.setId(USER_ID);
            Message message = new Message(
                body + i,
                user,
                group,
                TIME);
            message.setId(MESSAGE_ID);
            messages.add(message);
        }
        return messages;
    }

    @Test
    public void limitIsZeroTest()
    {
        int offset  = 0;
        int limit = MessageController.DEFAULT_LIMIT;
        Mockito.
            when(messageDAO.findMessageWithGroupID(GROUP_ID,offset,limit)).
            thenReturn(messages.subList(offset,limit));


        Response response = controller.getMessagesForGroup(GROUP_ID,0,0);
        Response validResponse = Response.status(Response.Status.OK).entity(messageDTOS.subList(offset,offset + limit)).build();

        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(), validResponse.getEntity());
    }

    @Test
    public void limitIsGreaterThanTheMaxLimitTest()
    {
        int offset  = 0;
        int limit = MessageController.MAX_LIMIT;
        Mockito.
            when(messageDAO.findMessageWithGroupID(GROUP_ID,offset,limit)).
            thenReturn(messages.subList(offset,offset +limit));


        Response response = controller.getMessagesForGroup(GROUP_ID,0,MessageController.MAX_LIMIT*2);
        Response validResponse = Response.status(Response.Status.OK).entity(messageDTOS.subList(offset,offset + limit)).build();

        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(), validResponse.getEntity());
    }

    @Test
    public void offsetLessThanZeroTest()
    {
        int offset  = 0;
        int limit = MessageController.DEFAULT_LIMIT;
        Mockito.
            when(messageDAO.findMessageWithGroupID(GROUP_ID,offset,limit)).
            thenReturn(messages.subList(offset,offset +limit));


        Response response = controller.getMessagesForGroup(GROUP_ID,-2,MessageController.DEFAULT_LIMIT);
        Response validResponse = Response.status(Response.Status.OK).entity(messageDTOS.subList(offset,offset + limit)).build();

        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(), validResponse.getEntity());
    }

}
