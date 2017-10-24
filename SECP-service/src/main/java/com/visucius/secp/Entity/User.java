package com.visucius.secp.Entity;

public class User {

    private long Id;
    private String firstName;
    private String lastName;
    private int permissionLevel;
    private String email;

    public User()
    {
    }

    public int getPermissionLevel() {
        return this.permissionLevel;
    }
}
