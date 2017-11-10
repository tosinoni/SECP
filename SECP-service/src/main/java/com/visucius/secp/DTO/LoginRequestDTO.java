package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestDTO {
    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    public  LoginRequestDTO() {
        this(null, null);
    }

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
