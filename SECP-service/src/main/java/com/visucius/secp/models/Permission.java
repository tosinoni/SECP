package com.visucius.secp.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Permissions")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.visucius.secp.models.Permission.findByName",
            query = "from Permission p where p.level = :name"
        )
    }
)
public class Permission {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;


    @Column(name = "level",unique = true, nullable = false)
    private String level;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public Permission()
    {

    }

    public Permission(String level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission permission = (Permission) o;
        return id == permission.id
            && permission.level.equals(this.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.level, this.id);
    }
}
