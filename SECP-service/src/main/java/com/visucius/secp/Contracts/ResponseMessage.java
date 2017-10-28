package com.visucius.secp.Contracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResponseMessage {

    @JsonIgnore
    public boolean success;
    @JsonProperty
    private String message;

    public ResponseMessage()
    {

    }

    public ResponseMessage(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }
}
