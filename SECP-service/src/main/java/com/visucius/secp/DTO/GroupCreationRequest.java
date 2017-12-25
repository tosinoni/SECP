package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class GroupCreationRequest {

    @JsonProperty
    public String name;
    @JsonProperty
    public Set<Integer> users;
    @JsonProperty
    public Set<Integer> roles;

    public GroupCreationRequest()
    {

    }

    public GroupCreationRequest(String name, Set<Integer> users, Set<Integer> roles)
    {
        this.name = name;
        this.users = users;
        this.roles = roles;
    }
}
