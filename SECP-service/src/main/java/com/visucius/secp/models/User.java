package com.visucius.secp.models;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.*;

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
        )
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

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "loginRole", nullable = false, columnDefinition = "varchar(32) default 'NORMAL'")
    private LoginRole loginRole = LoginRole.NORMAL;

    @ManyToMany
    @JoinTable(name = "user_roles",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(mappedBy = "users")
    private Set<Group> groups = new HashSet<>();

    public  User () {

    }
    public User(String firstname, String lastname, String userName, String email, String password) {
        this.firstname = firstname;
        this.username = userName;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
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
