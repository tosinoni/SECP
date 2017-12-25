package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class GroupCreationRequest {

    @JsonProperty
    public String name;
    @JsonProperty
    public Set<Long> users;
    @JsonProperty
    public Set<Long> roles;

    public GroupCreationRequest()
    {

    }

    public GroupCreationRequest(String name, Set<Long> users, Set<Long> roles)
    {
        this.name = name;
        this.users = users;
        this.roles = roles;
    }
}
