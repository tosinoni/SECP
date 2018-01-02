package com.visucius.secp.DTO;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MessageDTOTest {

    private long messageId = 1;
    private long groupId = 1;
    private long senderId = 1;
    private Date timestamp = new Date();
    private String body = "test";
    private MessageDTO.MessageType reason = MessageDTO.MessageType.MESSAGE;

    @Test
    public void testGroupID() {
        MessageDTO messageDTO = new MessageDTO(messageId,groupId,senderId,body,reason);
        assertEquals("group id is not equal", groupId, messageDTO.getGroupId());
    }

    @Test
    public void testTimeStamp() {
        MessageDTO messageDTO = new MessageDTO(messageId,groupId,senderId,body,reason);
        messageDTO.setTimestamp(timestamp);
        assertEquals("timestamp is not equal", timestamp, messageDTO.getTimestamp());
    }

    @Test
    public void testBody() {
        MessageDTO messageDTO = new MessageDTO(messageId,groupId,senderId,body,reason);
        assertEquals("Body is not equal", body, messageDTO.getBody());
    }


    @Test
    public void testReason() {
        MessageDTO messageDTO = new MessageDTO(messageId,groupId,senderId,body,reason);
        assertEquals("reason is not equal", reason, messageDTO.getReason());
    }

    @Test
    public void testEquals() {
        MessageDTO messageDTO = new MessageDTO(messageId,groupId,senderId,body,reason);
        messageDTO.setTimestamp(timestamp);
        MessageDTO messageDTO2 = new MessageDTO(messageId,groupId,senderId,body,reason);
        messageDTO2.setTimestamp(timestamp);
        assertEquals(messageDTO,messageDTO2);
    }

    @Test
    public void testNotEquals() {
        MessageDTO messageDTO = new MessageDTO(messageId,groupId,senderId,body,reason);
        messageDTO.setTimestamp(timestamp);
        MessageDTO messageDTO2 = new MessageDTO(2,3,senderId,body,reason);
        messageDTO2.setTimestamp(new Date());
        assertNotEquals(messageDTO,messageDTO2);
    }
}
