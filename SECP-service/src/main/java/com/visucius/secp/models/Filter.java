package com.visucius.secp.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Filters")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.visucius.secp.models.Filter.findByName",
            query = "from Filter f where f.name = :name"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.Filter.findAllFilters",
            query = "select f from Filter f"
        )
    }
)
public class Filter {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany()
    @JoinTable(name = "filter_roles",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "filter_permissions",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    private Set<Permission> permissions = new HashSet<>();


    public Filter()
    {
    }

    public Filter(String name) {

        this.name = name;
    }

    public long getId(){return id;}

    public void setId(long id){this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles){this.roles = roles;}

    public void setPermissions(Set<Permission> permissions){this.permissions = permissions;}

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public void addPermissions(Collection<Permission> permissions)
    {
        this.permissions.addAll(permissions);
    }

    public void addRoles(Collection<Role> roles)
    {
        this.roles.addAll(roles);
    }

    public void removeRoles(Collection<Role> roles)
    {
        this.roles.removeAll(roles);
    }

    public void removePermissions(Collection<Permission> permissions)
    {
        this.permissions.removeAll(permissions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Filter)) return false;
        Filter group = (Filter) o;
        return id == group.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id,this.name);
    }
}
