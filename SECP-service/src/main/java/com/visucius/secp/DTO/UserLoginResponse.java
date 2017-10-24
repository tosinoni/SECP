package com.visucius.secp.DTO;

import com.visucius.secp.Contracts.ResponseMessage;
import javax.ws.rs.core.Response;

public class UserLoginResponse extends ResponseMessage {

    public Response.Status status;
    public String accessToken;

    public UserLoginResponse(boolean success, String message, Response.Status status, String accessToken)
    {
        super(success,message);
        this.status = status;
        this.accessToken = accessToken;
    }
}
