package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.models.Secret;

import java.util.Objects;

public class SecretDTO {
    @JsonProperty
    private long groupID;

    @JsonProperty
    private long userID;

    @JsonProperty
    private long deviceID;

    @JsonProperty
    private String encryptedSecret;

    public SecretDTO() {

    }

    public SecretDTO(long groupID, long userID, long deviceID, String encryptedSecret) {
        this.groupID = groupID;
        this.encryptedSecret = encryptedSecret;
        this.deviceID = deviceID;
        this.userID = userID;
    }

    public SecretDTO(Secret secret) {
        if (secret != null) {
            this.groupID = secret.getGroupID();
            this.encryptedSecret = secret.getEncryptedSecret();
            this.userID = secret.getUserID();

            if (secret.getDevice() != null) {
                this.deviceID = secret.getDevice().getId();
            }
        }
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

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.groupID, this.userID, this.encryptedSecret, this.groupID, this.deviceID);
    }
}
