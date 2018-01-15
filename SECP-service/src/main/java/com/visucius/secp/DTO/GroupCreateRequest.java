package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class GroupCreateRequest {

    @JsonProperty
    public String name;
    @JsonProperty
    public Set<RolesOrPermissionDTO> permissions = new HashSet<>();
    @JsonProperty
    public Set<RolesOrPermissionDTO> roles = new HashSet<>();

    public GroupCreateRequest()
    {

    }

    public GroupCreateRequest(String name, Set<RolesOrPermissionDTO> permissions, Set<RolesOrPermissionDTO> roles)
    {
        this.name = name;
        this.permissions = permissions;
        this.roles = roles;
    }
}
