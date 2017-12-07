package com.visucius.secp.DTO;

import com.visucius.secp.models.LoginRole;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenDTOTest {

    private final String username = "user1";
    private final String token = "token";
    private final long userID = 42;
    private final LoginRole loginRole = LoginRole.ADMIN;


    @Test
    public void testUsername() {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUsername(username);
        assertEquals("username is not equal",username, tokenDTO.getUsername());
    }

    @Test
    public void testloginRole() {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setLoginRole(loginRole);
        assertEquals("loginrole is not equal",loginRole, tokenDTO.getLoginRole());
    }

    @Test
    public void testUserID() {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setUserID(userID);
        assertEquals("userID is not equal",userID, tokenDTO.getUserID());
    }

    @Test
    public void testToken() {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(token);
        assertEquals("userID is not equal",token, tokenDTO.getToken());
    }

    @Test
    public void testConstructor() {
        TokenDTO tokenDTO = new TokenDTO(userID, username, token, loginRole);
        assertEquals("userID is not equal",token, tokenDTO.getToken());
        assertEquals("userID is not equal",userID, tokenDTO.getUserID());
        assertEquals("loginrole is not equal",loginRole, tokenDTO.getLoginRole());
        assertEquals("username is not equal",username, tokenDTO.getUsername());
    }

}
