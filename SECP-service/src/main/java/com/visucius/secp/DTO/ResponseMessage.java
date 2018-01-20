package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResponseMessage {

    @JsonIgnore
    private boolean success;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
