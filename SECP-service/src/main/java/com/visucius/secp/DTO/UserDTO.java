package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.User;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {

    public static final String defaultUserAvatar = "https://user-images.githubusercontent.com/14824913/34922743-f386cabc-f961-11e7-84af-be3f61f41005.png";


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
    private String displayName;

    @JsonProperty
    private String avatarUrl;

    @JsonProperty
    private Set<DeviceDTO> devices = new HashSet<>();

    @JsonProperty
    private LoginRole loginRole = LoginRole.NORMAL;

    public UserDTO()
    {

    }

    public UserDTO(long userID) {
        this.userID = userID;
    }

    public UserDTO(User user){
        this.userID = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstname();
        this.lastName = user.getLastname();
        this.avatarUrl = getAvatarForUser(user);
        this.displayName = getDisplayNameForUser(user);

        Set<RolesOrPermissionDTO> roles = user.getRoles().stream()
            .map(role -> {
                return new RolesOrPermissionDTO(role.getId(), role.getRole());
            }).collect(Collectors.toSet());

        Permission userPermission = user.getPermission();

        RolesOrPermissionDTO permissionForUser = new RolesOrPermissionDTO();
        if(userPermission != null) {
            permissionForUser.setId(userPermission.getId());
            permissionForUser.setName(userPermission.getLevel());
        }

        this.permission = permissionForUser;
        this.numOfRoles = user.getRoles().size();
        this.roles = roles;
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

    public String getDisplayName(){ return displayName; }

    public void setDisplayName(String displayName){ this.displayName = displayName; }

    public String getAvatarUrl(){ return avatarUrl; }

    public void setAvatarUrl(String avatarUrl){ this.avatarUrl = avatarUrl;}

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

    public LoginRole getLoginRole() {
        return loginRole;
    }

    public void setLoginRole(LoginRole loginRole) {
        this.loginRole = loginRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO u = (UserDTO) o;
        return userID == u.userID && devices.equals(u.devices);
    }

    private String getAvatarForUser(User user) {
        String avatarUrl = user.getAvatarUrl();

        if(StringUtils.isEmpty(avatarUrl)) {
            avatarUrl =  defaultUserAvatar;
        }

        return avatarUrl;
    }

    private String getDisplayNameForUser(User user) {
        String displayName = user.getDisplayName();

        if(StringUtils.isEmpty(displayName)) {
            displayName =  user.getUsername();
        }

        return displayName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userID, this.devices);
    }
}
