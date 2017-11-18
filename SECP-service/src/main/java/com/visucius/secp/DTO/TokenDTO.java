package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.visucius.secp.models.LoginRole;

public class TokenDTO {

    @JsonProperty
    private String token;

    @JsonProperty
    private LoginRole loginRole = LoginRole.NORMAL;

    public TokenDTO(String token, LoginRole loginRole) {
        this.token = token;
        this.loginRole = loginRole;
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
}
