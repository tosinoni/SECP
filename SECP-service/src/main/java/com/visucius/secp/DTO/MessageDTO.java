package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Objects;

public class MessageDTO {

    @JsonProperty
    private long messageId;

    @JsonProperty
    private long groupId;

    @JsonProperty
    private long senderId;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private String body;

    @JsonProperty
    private String senderDisplayName;

    @JsonProperty
    private MessageType reason;

    public MessageDTO()
    {

    }

    public MessageDTO(long messageId, long groupId, long senderId, String body, MessageType reason, Date timestamp)
    {
        this.messageId = messageId;
        this.groupId = groupId;
        this.senderId = senderId;
        this.body = body;
        this.reason = reason;
        this.timestamp = new Date(timestamp.getTime());
    }

    public long getGroupId() {
        return this.groupId;
    }

    public MessageType getReason() {
        return this.reason;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = new Date(timestamp.getTime());
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getBody() {
        return body;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getSenderId() {
        return senderId;
    }

    public String getSenderDisplayName() {
        return senderDisplayName;
    }

    public void setSenderDisplayName(String senderDisplayName) {
        this.senderDisplayName = senderDisplayName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MessageDTO)) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;

        return messageDTO.messageId == this.messageId
            && this.body.equals(messageDTO.body) && this.groupId == messageDTO.groupId
            && this.timestamp.equals(messageDTO.timestamp) && this.senderId == messageDTO.senderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.body,this.messageId,this.senderId,this.groupId,this.timestamp);
    }

    public enum MessageType
    {
        @JsonProperty("message")
        MESSAGE,
        @JsonProperty("user_authorization")
        USER_AUTHORIZATION,
        @JsonProperty("admin_authoriztion")
        ADMIN_AUTHORIZATION,
    }
}
