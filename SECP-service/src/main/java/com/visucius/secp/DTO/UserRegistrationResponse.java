package com.visucius.secp.DTO;


import com.visucius.secp.Contracts.ResponseMessage;

import javax.ws.rs.core.Response;

public class UserRegistrationResponse extends ResponseMessage {

    public Response.Status status;

    public UserRegistrationResponse(boolean success, String message, Response.Status status)
    {
        super(success,message);
        this.status = status;
    }
}
