package com.visucius.secp.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@NamedQueries(
    {
        @NamedQuery(
            name = "com.visucius.secp.models.User.findByUsername",
            query = "from User u where u.username = :username"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.User.findByEmail",
            query = "from User u where u.email = :email"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.User.findUsersWithRole",
            query = "select u from User u join u.roles r where r.id = :roleID"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.User.findUsersWithPermissionLevel",
            query = "select u from User u join u.permissions p where p.id = :permissionID"
        ),
        @NamedQuery(
            name = "com.visucius.secp.models.User.findAdmins",
            query = "from User u where u.loginRole = :loginRole"
        ),
    }
)
public class User implements Principal {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Email
    @Column(name = "email",unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "display_name", nullable = false)
    private String display_name;

    @URL
    @Column(name = "avatar_url", nullable = false)
    private String avatar_url;

    @ManyToMany
    @JoinTable(name = "user_roles",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Group> groups = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "user_permissions",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "permission_id") })
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_devices",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "device_id") })
    private Set<Device> devices = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    @Column(name = "login_role", nullable = false)
    private LoginRole loginRole = LoginRole.NORMAL;

    public  User () {

    }
    public User(String firstname, String lastname, String userName, String email, String password) {
        this.firstname = firstname;
        this.username = userName;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
    }

    public User(String userName, String email) {
        this(null, null, userName, email, null);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public String getDisplayName(){ return display_name; }

    public void setDisplayName(String displayname){ this.display_name = displayname; }

    public String getAvatar_url(){ return avatar_url; }

    public void setAvatar_url(String avatar_url){ this.avatar_url = avatar_url; }

    /*
     * The below three methods are needed for authentication and authorization
    */
    @Override
    public String getName() {
        return username;
    }

    public LoginRole getLoginRole() {
        return loginRole;
    }

    public void setLoginRole(LoginRole loginRole) {
        this.loginRole = loginRole;
    }

    public Set<Device> getDevices() {
        return devices;
    }

    public void setDevices(Set<Device> devices) {
        this.devices = devices;
    }

    public void addDevice (Device device) {
        devices.add(device);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
