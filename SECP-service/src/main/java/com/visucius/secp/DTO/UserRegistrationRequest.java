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

    public UserRegistrationRequest(String firstName, String lastName, String userName , String email, String password)
    {
        this.firstName = firstName;
        this.lastName =lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.setAvatar_url(avatar_url);
        this.setDisplayName(displayName);
    }

    public String getDisplayName(){ return this.displayName; }

    public void setDisplayName(String displayName){ this.displayName = displayName; }

    public String getAvatar_url(){ return avatar_url; }

    public void setAvatar_url(String avatar_url){ this.avatar_url = avatar_url;}
}
