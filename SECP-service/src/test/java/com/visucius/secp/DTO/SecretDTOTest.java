package com.visucius.secp.DTO;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SecretDTOTest {
    @Test
    public void testGroupID() {
        SecretDTO secretDTO = new SecretDTO();
        secretDTO.setGroupID(1);
        assertEquals("group id is not equal", 1, secretDTO.getGroupID());
    }

    @Test
    public void testUserID() {
        SecretDTO secretDTO = new SecretDTO();
        secretDTO.setUserID(1);
        assertEquals("user id is not equal", 1, secretDTO.getUserID());
    }

    @Test
    public void testDeviceID() {
        SecretDTO secretDTO = new SecretDTO();
        secretDTO.setDeviceID(1);
        assertEquals("device id is not equal", 1, secretDTO.getDeviceID());
    }

    @Test
    public void testEncryptedSecret() {
        SecretDTO secretDTO = new SecretDTO();
        secretDTO.setEncryptedSecret("secret");
        assertEquals("encrypted secret is not equal", "secret", secretDTO.getEncryptedSecret());
    }

    @Test
    public void testConstructor() {
        SecretDTO secretDTO = new SecretDTO(1, 1, 1, "hello");
        assertEquals("group id is not equal", 1, secretDTO.getGroupID());
        assertEquals("user id is not equal", 1, secretDTO.getUserID());
        assertEquals("device id is not equal", 1, secretDTO.getDeviceID());
        assertEquals("encrypted secret is not equal", "hello", secretDTO.getEncryptedSecret());
    }
}
