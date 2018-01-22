package com.visucius.secp.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "secret")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.visucius.secp.models.Secret.findSecretForDevice",
            query = "from Secret s where s.groupID = :groupID and s.device.id = :deviceID"
        )
    }
)
public class Secret {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "group_id", nullable = false, unique = true)
    private long groupID;

    @Lob
    @Column(name = "encrypted_secret", nullable = false)
    private String encryptedSecret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    public Secret()
    {
    }

    public Secret(long groupID, String encryptedSecret, Device device) {

        this.encryptedSecret = encryptedSecret;
        this.groupID = groupID;
        this.device = device;
    }

    public long getId(){return id;}

    public void setId(long id){this.id = id;}

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

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Secret)) return false;
        Secret groupSecret = (Secret) o;
        return id == groupSecret.id && groupID == groupSecret.groupID && encryptedSecret.equals(groupSecret.encryptedSecret)
            && device.equals(groupSecret.device);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id,this.encryptedSecret, this.groupID, this.device);
    }
}
