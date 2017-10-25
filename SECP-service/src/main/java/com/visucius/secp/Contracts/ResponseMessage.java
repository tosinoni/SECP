package com.visucius.secp.Contracts;

public abstract class ResponseMessage {

    public boolean success;
    public String message;

    protected ResponseMessage(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }
}
