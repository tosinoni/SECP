package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.models.Group;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDTO {
    @JsonProperty
    private long userID;

    @JsonProperty
    private String username;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private long numOfRoles;

    @JsonProperty
    private long numOfPermissions;

    @JsonProperty
    private Set<GroupDTO> groups;

    @JsonProperty
    private long numOfGroups;

    @JsonProperty
    private Set<RolesOrPermissionDTO> roles;

    @JsonProperty
    private Set<RolesOrPermissionDTO> permissions;


    @JsonProperty
    private String displayName;

    @JsonProperty
    private String avatar_url;

    @JsonProperty
    private Set<DeviceDTO> devices = new HashSet<>();

    public UserDTO(long userID) {
        this.userID = userID;
    }

    public UserDTO(long userID, Set<DeviceDTO> devices) {
        this.userID = userID;
        this.devices = devices;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Set<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupDTO> groups) {
        this.groups = groups;
    }

    public long getNumOfGroups() {
        return numOfGroups;
    }

    public void setNumOfGroups(long numOfGroups) {
        this.numOfGroups = numOfGroups;
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

    public String getDisplayName(){ return displayName; }

    public void setDisplayName(String displayName){ this.displayName = displayName; }

    public String getAvatar_url(){ return avatar_url; }

    public void setAvatar_url(String avatar_url){ this.avatar_url = avatar_url;}

    public Set<DeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceDTO> devices) {
        this.devices = devices;
    }

    public void addDevice(DeviceDTO device) {
        devices.add(device);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO u = (UserDTO) o;
        return userID == u.userID && devices.equals(u.devices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userID, this.devices);
    }
}
