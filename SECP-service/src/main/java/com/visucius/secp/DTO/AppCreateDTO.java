package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class AppCreateDTO {

    @JsonProperty
    private Set<String> roles = new HashSet<>();

    @JsonProperty
    private Set<String> permissions = new HashSet<>();

    public AppCreateDTO() {

    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }
}
