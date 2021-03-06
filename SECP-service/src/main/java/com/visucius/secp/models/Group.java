package com.visucius.secp.models;

import org.hibernate.validator.constraints.URL;

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
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.Group.findAllPublicGroups",
            query = "select g from Group g where g.groupType = :type"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.Group.findGroupsForUser",
            query = "select g from Group g join g.users u where u.id = :userID and g.groupType = 'PUBLIC'"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.Group.findPrivateGroupForUsers",
            query = "select g from Group g inner join g.users u where u in :users and g.groupType = 'PRIVATE' group by g having count (u) = 2"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.Group.search",
            query = "from Group g where lower(g.name) like lower(:value)"
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
    @JoinTable(name = "group_permissions",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "group_user",
        joinColumns = { @JoinColumn(name = "group_id") },
        inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private Set<Message> messages = new HashSet<>();

    @Column(name = "isActive", nullable = false)
    private boolean isActive = true;

    @URL
    @Column(name = "avatar_url", nullable = false)
    private String avatarurl;

    @Column(name = "display_name", nullable = false)
    private String displayname;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    private GroupType groupType = GroupType.PUBLIC;

    public Group()
    {
    }

    public Group(String name) {

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

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users){this.users = users;}

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles){this.roles = roles;}

    public void setPermissions(Set<Permission> permissions){this.permissions = permissions;}

    public Set<Message> getMessages() {return this.messages;}

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public void setMessages(Set<Message> messages){this.messages = messages;}

    public void setIsActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public void addUser(User user)
    {
        this.users.add(user);
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    public boolean isActive() {
        return isActive;
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
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
