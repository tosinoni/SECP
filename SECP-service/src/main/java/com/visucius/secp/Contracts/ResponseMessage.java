package com.visucius.secp.Contracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResponseMessage {

    @JsonIgnore
    public boolean success;
    @JsonProperty
    @edu.umd.cs.findbugs.annotations.SuppressWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
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
