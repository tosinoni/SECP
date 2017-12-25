package com.visucius.secp.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "permissionLevel")
public class PermissionLevel {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;


    @Column(name = "level", nullable = false)
    private String level;

    @ManyToMany(mappedBy = "permissionLevels", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(mappedBy = "permissionLevels", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public PermissionLevel()
    {

    }

    public PermissionLevel(String level) {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionLevel)) return false;
        PermissionLevel permissionLevel = (PermissionLevel) o;
        return id == permissionLevel.id
            && permissionLevel.level.equals(this.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.level, this.id);
    }
}
