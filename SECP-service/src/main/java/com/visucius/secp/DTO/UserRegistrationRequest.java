package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

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

    @JsonProperty
    public RolesOrPermissionDTO permission;

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
    }

    public UserRegistrationRequest(String firstName, String lastName, String userName , String email, String password, RolesOrPermissionDTO permission)
    {
        this.firstName = firstName;
        this.lastName =lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.permission = permission;
    }
}
