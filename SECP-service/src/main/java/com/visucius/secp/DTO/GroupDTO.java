package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GroupDTO {
    @JsonProperty
    private long groupID;

    @JsonProperty
    private String name;

    @JsonProperty
    private long numOfRoles;

    @JsonProperty
    private long numOfPermissions;

    @JsonProperty
    private long numOfUsers;

    @JsonProperty
    private boolean isActive = true;

    @JsonProperty
    private Set<RolesOrPermissionDTO> roles = new HashSet<>();

    @JsonProperty
    private Set<RolesOrPermissionDTO> permissions = new HashSet<>();

    @JsonProperty
    private Set<UserDTO> users = new HashSet<>();

    public GroupDTO()
    {
    }

    public GroupDTO(long groupID) {
        this.groupID = groupID;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public void addUser(UserDTO user) {
        this.users.add(user);
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

    public long getNumOfUsers() {
        return numOfUsers;
    }

    public void setNumOfUsers(long numOfUsers) {
        this.numOfUsers = numOfUsers;
    }

    public boolean isActive() {
        return isActive;
    }

    @JsonIgnore
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupDTO)) return false;
        GroupDTO g = (GroupDTO) o;

        return groupID == g.groupID && users.equals(g.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.groupID, this.users);
    }
}
