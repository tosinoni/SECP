package com.visucius.secp.Contracts;

public abstract class ResponseMessage {

    public boolean Success;
    public String Message;

    protected ResponseMessage(boolean success, String message)
    {
        this.Success = success;
        this.Message = message;
    }
}
