package com.visucius.secp.DTO;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.visucius.secp.util.JsonUtil;

import javax.ws.rs.core.Response;
import java.util.List;

public class UserRegistrationResponse extends ResponseMessage {

    @JsonIgnore
    private Response.Status status;

    @JsonIgnore
    private List<String> errors;

    @JsonProperty
    private long userID;

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

    public long getUserID() {
        return this.userID;
    }

    public String toString()
    {
        try {
            return JsonUtil.convertToJsonString(this);
        }
        catch (JsonProcessingException e)
        {
            return "";
        }
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }
}
