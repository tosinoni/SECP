package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FilterDTO {
    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private long numOfRoles;

    @JsonProperty
    private long numOfPermissions;

    @JsonProperty
    private Set<RolesOrPermissionDTO> roles = new HashSet<>();

    @JsonProperty
    private Set<RolesOrPermissionDTO> permissions = new HashSet<>();

    public FilterDTO()
    {
    }

    public FilterDTO(long filterID) {
        this.id = filterID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RolesOrPermissionDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RolesOrPermissionDTO> roles) {
        this.roles = roles;
    }

    public Set<RolesOrPermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<RolesOrPermissionDTO> permissions) {
        this.permissions = permissions;
    }

    public long getNumOfRoles() {
        return numOfRoles;
    }

    public void setNumOfRoles(long numOfRoles) {
        this.numOfRoles = numOfRoles;
    }

    public long getNumOfPermissions() {
        return numOfPermissions;
    }

    public void setNumOfPermissions(long numOfPermissions) {
        this.numOfPermissions = numOfPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterDTO)) return false;
        FilterDTO g = (FilterDTO) o;

        return id == g.id && name.equals(g.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }
}
