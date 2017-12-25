package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class GroupCreationRequest {

    @JsonProperty
    public String name;
    @JsonProperty
    public Set<Long> permissionLevels;
    @JsonProperty
    public Set<Long> roles;

    public GroupCreationRequest()
    {

    }

    public GroupCreationRequest(String name, Set<Long> permissionLevels, Set<Long> roles)
    {
        this.name = name;
        this.permissionLevels = permissionLevels;
        this.roles = roles;
    }
}
