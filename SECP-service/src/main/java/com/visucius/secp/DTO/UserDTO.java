package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Set<GroupDTO> groups;

    @JsonProperty
    private long numOfGroups;

    @JsonProperty
    private boolean isActive = true;

    @JsonProperty
    private Set<RolesOrPermissionDTO> roles;

    @JsonProperty
    private RolesOrPermissionDTO permission;

    @JsonProperty
    private Set<DeviceDTO> devices = new HashSet<>();

    public UserDTO()
    {

    }

    public UserDTO(long userID) {
        this.userID = userID;
    }

    public UserDTO(long userID, Set<DeviceDTO> devices) {
        this.userID = userID;
        this.devices = devices;
    }


    public UserDTO(long userID, Set<DeviceDTO> devices, String username) {
        this(userID,devices);
        this.username = username;
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

    public RolesOrPermissionDTO getPermission() {
        return permission;
    }

    public void setPermission(RolesOrPermissionDTO permission) {
        this.permission = permission;
    }

    public Set<DeviceDTO> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceDTO> devices) {
        this.devices = devices;
    }

    public void addDevice(DeviceDTO device) {
        devices.add(device);
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
        if (!(o instanceof UserDTO)) return false;
        UserDTO u = (UserDTO) o;
        return userID == u.userID && devices.equals(u.devices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userID, this.devices);
    }
}
