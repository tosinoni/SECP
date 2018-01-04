package com.visucius.secp.DTO;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RolesOrPermissionDTOTest {

    private final long id = 1;
    private final String name = "Developer";

    @Test
    public void testID() {
        RolesOrPermissionDTO rolesOrPermissionDTO = new RolesOrPermissionDTO();
        rolesOrPermissionDTO.setId(id);
        assertEquals("id is not equal", id, rolesOrPermissionDTO.getId());
    }

    @Test
    public void testName() {
        RolesOrPermissionDTO rolesOrPermissionDTO = new RolesOrPermissionDTO();
        rolesOrPermissionDTO.setName(name);
        assertEquals("name is not equal", name, rolesOrPermissionDTO.getName());
    }

    @Test
    public void testConstructor() {
        RolesOrPermissionDTO rolesOrPermissionDTO = new RolesOrPermissionDTO(id, name);
        assertEquals("id is not equal", id, rolesOrPermissionDTO.getId());
        assertEquals("name is not equal", name, rolesOrPermissionDTO.getName());
    }

}
