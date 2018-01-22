package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.models.Device;

import java.util.Objects;

public class DeviceDTO {

    @JsonProperty
    private long deviceID;

    @JsonProperty
    private String deviceName;

    @JsonProperty
    private String publicKey;

    @JsonProperty
    private long userID;

    public DeviceDTO() {

    }

    public DeviceDTO(long deviceID, String publicKey) {
        this.deviceID = deviceID;
        this.publicKey = publicKey;
    }

    public DeviceDTO(long userID, String deviceName, String publicKey) {
        this.userID = userID;
        this.deviceName = deviceName;
        this.publicKey = publicKey;
    }

    public DeviceDTO(Device device) {
        if (device != null) {
            this.deviceID = device.getId();
            this.deviceName = device.getName();
            this.publicKey = device.getPublicKey();
        }
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(long deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeviceDTO)) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return deviceID == deviceDTO.deviceID && userID == deviceDTO.userID && deviceName.equals(deviceDTO.deviceName)
            && publicKey.equals(deviceDTO.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.deviceID, this.deviceName, this.publicKey, this.userID);
    }
}
