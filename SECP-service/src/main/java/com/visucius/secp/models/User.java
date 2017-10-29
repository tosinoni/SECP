package com.visucius.secp.models;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.Set;

@Entity
@Table(name = "users")
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
public class User {

    @Id
    @GeneratedValue
    @Column(name = "user_id", unique = true, nullable = false)
    private long id;

    @NotNull
    @Column(name = "username", unique = true, nullable = false, length = 20)
    private String username;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Email
    @Column(name = "email",unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<UserRole>  userRoles;

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

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
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
