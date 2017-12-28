package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class GroupModifyRequest {

    @JsonProperty
    public Set<Long> roles = new HashSet<>();

    @JsonProperty
    public Set<Long> permissions = new HashSet<>();

    public GroupModifyRequest()
    {

    }

    public GroupModifyRequest(
        Set<Long> permissions,
        Set<Long> roles)
    {
        this.permissions = permissions;
        this.roles = roles;
    }
}
