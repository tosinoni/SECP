package com.visucius.secp.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.Contracts.IRequest;

public class UserLoginRequest implements IRequest<UserLoginResponse> {

    @JsonProperty
    public String email;

    @JsonProperty
    public String password;

    public UserLoginRequest()
    {

    }
}
