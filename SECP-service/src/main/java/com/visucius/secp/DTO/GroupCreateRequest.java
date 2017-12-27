package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class GroupCreateRequest {

    @JsonProperty
    public String name;
    @JsonProperty
    public Set<Long> permissions;
    @JsonProperty
    public Set<Long> roles;

    public GroupCreateRequest()
    {

    }

    public GroupCreateRequest(String name, Set<Long> permissions, Set<Long> roles)
    {
        this.name = name;
        this.permissions = permissions;
        this.roles = roles;
    }
}
