package com.visucius.secp.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Roles")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.visucius.secp.models.Role.findByName",
            query = "from Role r where r.role = :name"
        )
    }
)
public class Role {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "role", unique = true, nullable = false)
    private String role;

    @Column(name = "color", nullable = false)
    private String color;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    public Role()
    {

    }

    public Role(String role) {
        this.role = role;
    }

    public Role(String role, String color) {
        this.role = role;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
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
        if (!(o instanceof Role)) return false;
        Role r = (Role) o;
        return id == r.id && role.equals(r.role) && users.equals(r.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.role, this.id);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
