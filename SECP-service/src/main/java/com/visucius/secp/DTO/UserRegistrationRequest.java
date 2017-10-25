package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.Contracts.IRequest;

public class UserRegistrationRequest implements IRequest<UserRegistrationResponse> {

    @JsonProperty
    public String firstName;

    @JsonProperty
    public String lastName;

    @JsonProperty
    public int permissionLevel;

    @JsonProperty
    public String email;

    @JsonProperty
    public String password;

    public UserRegistrationRequest()
    {

    }
}
