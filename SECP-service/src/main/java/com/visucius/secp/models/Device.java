package com.visucius.secp.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Devices")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.visucius.secp.models.Device.findByDeviceName",
            query = "from Device d where d.name = :name"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.Device.getDevicesForUser",
            query = "select d FROM Device d join d.users u where u.id= :id"
        )
    }
)
public class Device {
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "public_key", nullable = false)
    private String publicKey;

    @ManyToMany(mappedBy = "devices", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "device", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Secret> groupSecrets = new HashSet<>();

    public Device() {

    }

    public Device(String name, String publicKey) {
        this.name = name;
        this.publicKey = publicKey;
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Set<Secret> getGroupSecrets() {
        return groupSecrets;
    }

    public void setGroupSecrets(Set<Secret> groupSecrets) {
        this.groupSecrets = groupSecrets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Device)) return false;
        Device d = (Device) o;
        return id == d.id && name.equals(d.name) && publicKey.equals(d.publicKey)
            && users.containsAll(d.users) && d.users.size() == users.size() && groupSecrets.equals(d.groupSecrets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.id, this.publicKey);
    }
}
