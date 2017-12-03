package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.models.LoginRole;

public class TokenDTO {

    @JsonProperty
    private long userID;

    @JsonProperty
    private String username;

    @JsonProperty
    private String token;

    @JsonProperty
    private LoginRole loginRole = LoginRole.NORMAL;

    public TokenDTO(long userID, String username, String token, LoginRole loginRole) {
        this.token = token;
        this.loginRole = loginRole;
        this.userID = userID;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginRole getLoginRole() {
        return loginRole;
    }

    public void setLoginRole(LoginRole loginRole) {
        this.loginRole = loginRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserID() {

        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
