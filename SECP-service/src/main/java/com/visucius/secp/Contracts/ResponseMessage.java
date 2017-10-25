package com.visucius.secp.Contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResponseMessage {

    @JsonProperty
    public boolean success;
    @JsonProperty
    public String message;

    public ResponseMessage()
    {

    }

    public ResponseMessage(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }
}
