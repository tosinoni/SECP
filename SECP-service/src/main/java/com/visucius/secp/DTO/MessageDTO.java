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
    private MessageType reason;

    public MessageDTO()
    {

    }

    public MessageDTO(long messageId, long groupId, long senderId, String body, MessageType reason)
    {
        this.messageId = messageId;
        this.groupId = groupId;
        this.senderId = senderId;
        this.body = body;
        this.reason = reason;
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
        return new Date(this.timestamp.getTime());
    }

    public String getBody() {
        return body;
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

    }
}
