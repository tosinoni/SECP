package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class GroupModifyRequest {

    @JsonProperty
    public Set<Long> remove_roles = new HashSet<>();
    @JsonProperty
    public Set<Long> add_roles = new HashSet<>();
    @JsonProperty
    public Set<Long> add_permissions = new HashSet<>();
    @JsonProperty
    public Set<Long> remove_permissions = new HashSet<>();

    public GroupModifyRequest()
    {

    }

    public GroupModifyRequest(
        Set<Long> add_permissions,
        Set<Long> remove_permissions,
        Set<Long> add_roles,
        Set<Long> remove_roles)
    {
        this.add_permissions = add_permissions;
        this.remove_permissions = remove_permissions;
        this.add_roles = add_roles;
        this.remove_roles = remove_roles;
    }
}
