package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistrationRequest {

    @JsonProperty
    public String firstName;

    @JsonProperty
    public String lastName;

    @JsonProperty
    public String userName;

    @JsonProperty
    public String email;

    @JsonProperty
    public String password;

    public UserRegistrationRequest()
    {

    }

    public UserRegistrationRequest(String firstName, String lastName, String userName ,String email, String password)
    {
        this.firstName = firstName;
        this.lastName =lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
