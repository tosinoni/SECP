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
    public String displayName;

    @JsonProperty
    public String email;

    @JsonProperty
    public String password;

    @JsonProperty
    public String avatar_url;

    public UserRegistrationRequest()
    {

    }

    public UserRegistrationRequest(String firstName, String lastName, String userName , String displayName, String email, String password)
    {
        this.firstName = firstName;
        this.lastName =lastName;
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
    }
}
