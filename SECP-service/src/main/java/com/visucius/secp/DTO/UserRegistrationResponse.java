package com.visucius.secp.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.Contracts.ResponseMessage;

import javax.ws.rs.core.Response;
import java.util.List;

public class UserRegistrationResponse extends ResponseMessage {

    @JsonIgnore
    public Response.Status status;

    @JsonIgnore
    public List<String> errors;

    @JsonProperty
    @edu.umd.cs.findbugs.annotations.SuppressWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
    public long userID;

    public UserRegistrationResponse(boolean success, String message, Response.Status status, List<String> errors)
    {
        super(success,message);
        this.status = status;
        this.errors = errors;
    }


    public UserRegistrationResponse(boolean success, String message, Response.Status status, List<String> errors, Long userID)
    {
        this(success,message,status,errors);
        this.userID = userID;
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
