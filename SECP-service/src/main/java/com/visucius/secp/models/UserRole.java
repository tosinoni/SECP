package com.visucius.secp.models;

import javax.persistence.*;

@Entity
@Table(name = "user_role",
       uniqueConstraints = @UniqueConstraint(columnNames={"role", "user_id"}))
public class UserRole {

    @Id
    @Column(name = "user_role_id", unique = true, nullable = false)
    private int roleId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "role", nullable = false, length = 45)
    private String role;


    public UserRole(User user, String role) {
        this.user = user;
        this.role = role;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRole)) return false;
        UserRole userRole = (UserRole) o;
        return roleId == userRole.roleId && user.equals(userRole.user) && role.equals(userRole.role);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + roleId;
        result = prime * result + user.hashCode();
        return result;
    }
}
