package com.visucius.secp.DTO;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AppCreateDTOTest {

    @Test
    public void testRoles() {
        AppCreateDTO appCreateDTO = new AppCreateDTO();

        Set<String> roles = new HashSet<>();
        roles.add("developer");

        appCreateDTO.setRoles(roles);
        assertEquals("roles are not equal",roles, appCreateDTO.getRoles());
    }

    @Test
    public void testPermissions() {
        AppCreateDTO appCreateDTO = new AppCreateDTO();

        Set<String> permissions = new HashSet<>();
        permissions.add("TOP_SECRET");

        appCreateDTO.setPermissions(permissions);
        assertEquals("permissions are not equal",permissions, appCreateDTO.getPermissions());
    }
}
