package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class SecretDTO {
    @JsonProperty
    private long groupID;

    @JsonProperty
    private long deviceID;

    @JsonProperty
    private String encryptedSecret;

    public SecretDTO() {

    }

    public SecretDTO(long groupID, long deviceID, String encryptedSecret) {
        this.groupID = groupID;
        this.encryptedSecret = encryptedSecret;
        this.deviceID = deviceID;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getEncryptedSecret() {
        return encryptedSecret;
    }

    public void setEncryptedSecret(String encryptedSecret) {
        this.encryptedSecret = encryptedSecret;
    }

    public long getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(long deviceID) {
        this.deviceID = deviceID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.groupID, this.encryptedSecret, this.groupID, this.deviceID);
    }
}
