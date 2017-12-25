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
    private int level;

    @ManyToMany(mappedBy = "permissionLevels", fetch = FetchType.LAZY)
    private Set<Group> groups = new HashSet<>();

    public PermissionLevel()
    {

    }

    public PermissionLevel(int level) {
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PermissionLevel)) return false;
        PermissionLevel permissionLevel = (PermissionLevel) o;
        return id == permissionLevel.id
            && permissionLevel.level == this.level;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.level, this.id);
    }
}
