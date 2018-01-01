package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MessageDTO {

    @JsonProperty
    public long messageId;

    @JsonProperty
    public long groupId;

    @JsonProperty
    public long userId;

    @JsonProperty
    public Date timestamp;

    @JsonProperty
    public String body;

    public MessageDTO()
    {

    }

    public MessageDTO(long messageId, long groupId, long userId, Date timestamp, String body)
    {
        this.messageId = messageId;
        this.groupId = groupId;
        this.userId = userId;
        this.timestamp = timestamp;
        this.body = body;
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
}
