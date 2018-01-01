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
    private long userId;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private String body;

    public MessageDTO()
    {

    }

    public MessageDTO(long messageId, long groupId, long userId, String body)
    {
        this.messageId = messageId;
        this.groupId = groupId;
        this.userId = userId;
        this.body = body;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = new Date(timestamp.getTime());
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
            && this.timestamp.equals(messageDTO.timestamp) && this.userId == messageDTO.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.body,this.messageId,this.userId,this.groupId,this.timestamp);
    }
}
