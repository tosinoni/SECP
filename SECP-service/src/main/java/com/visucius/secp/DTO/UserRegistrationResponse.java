package com.visucius.secp.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.Contracts.ResponseMessage;

import javax.ws.rs.core.Response;
import java.util.List;

public class UserRegistrationResponse extends ResponseMessage {

    public Response.Status status;
    @JsonProperty
    public List<String> errors;

    public UserRegistrationResponse(boolean success, String message, Response.Status status, List<String> errors)
    {
        super(success,message);
        this.status = status;
        this.errors = errors;
    }

    public String getErrors()
    {
        StringBuilder builder = new StringBuilder();
        for(String error: this.errors)
        {
            builder.append(error + "\n");
        }

        return builder.toString();
    }
}
