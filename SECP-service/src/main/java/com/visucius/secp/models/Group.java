package com.visucius.secp.models;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Groups")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.visucius.secp.models.Group.findByName",
            query = "from Group g where g.name = :name"
        )
    }
)
public class Group {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany()
    @JoinTable(name = "group_roles",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "group_permissionLevel",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "permissionLevels_id") })
    private Set<PermissionLevel> permissionLevels = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "group_user",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Message> messages = new HashSet<>();

    public Group()
    {

    }

    public Group(String name) {
        this.name = name;
    }

    public long getId(){return id;}

    public void setId(int id){this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users){this.users = users;}

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles){this.roles = roles;}

    public void setPermissionLevels(Set<PermissionLevel> permissions){this.permissionLevels = permissions;}

    public Set<Message> getMessages() {return this.messages;}

    public void setMessages(Set<Message> messages){this.messages = messages;}

    public void addPermissionLevels(Collection<PermissionLevel> permissions)
    {
        this.permissionLevels.addAll(permissions);
    }

    public void addRoles(Collection<Role> roles)
    {
        this.roles.addAll(roles);
    }

    public void addUsers(Collection<User> users)
    {
        this.users.addAll(users);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return id == group.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id,this.name);
    }
}
