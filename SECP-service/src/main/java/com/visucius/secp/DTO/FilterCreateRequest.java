package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class FilterCreateRequest {

    @JsonProperty
    public String name;

    @JsonProperty
    public Set<RolesOrPermissionDTO> permissions;

    @JsonProperty
    public Set<RolesOrPermissionDTO> roles;

    public FilterCreateRequest()
    {

    }

    public FilterCreateRequest(String name, Set<RolesOrPermissionDTO> permissions, Set<RolesOrPermissionDTO> roles)
    {
        this.name = name;
        this.permissions = permissions;
        this.roles = roles;
    }
}
