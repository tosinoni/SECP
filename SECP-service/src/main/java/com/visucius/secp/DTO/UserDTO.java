package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDTO {
    @JsonProperty
    private long userID;

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
