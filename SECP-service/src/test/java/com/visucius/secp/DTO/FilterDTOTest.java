package com.visucius.secp.DTO;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class FilterDTOTest {
    private final long filterID = 1;


    @Test
    public void testFilterID() {
        FilterDTO filterDTO = new FilterDTO(filterID);
        filterDTO.setId(filterID);
        assertEquals("filter id is not equal", filterID, filterDTO.getId());
    }

    @Test
    public void testName() {
        FilterDTO FilterDTO = new FilterDTO(filterID);
        FilterDTO.setName("developer");
        assertEquals("name is not equal", "developer", FilterDTO.getName());
    }

    @Test
    public void testNumberOfRoles() {
        FilterDTO FilterDTO = new FilterDTO(filterID);
        FilterDTO.setNumOfRoles(3);
        assertEquals("Number of roles not equal", 3, FilterDTO.getNumOfRoles());
    }

    @Test
    public void testNumberOfPermissions() {
        FilterDTO FilterDTO = new FilterDTO(filterID);
        FilterDTO.setNumOfPermissions(3);
        assertEquals("Number of permission levels not equal", 3, FilterDTO.getNumOfPermissions());
    }

    @Test
    public void testRoles() {
        FilterDTO FilterDTO = new FilterDTO(filterID);

        RolesOrPermissionDTO role = new RolesOrPermissionDTO(1, "tester");
        Set<RolesOrPermissionDTO> roles = new HashSet<>();
        roles.add(role);

        FilterDTO.setRoles(roles);
        assertEquals("roles are not equal", roles, FilterDTO.getRoles());
    }

    @Test
    public void testPermissions() {
        FilterDTO FilterDTO = new FilterDTO(filterID);

        RolesOrPermissionDTO permission = new RolesOrPermissionDTO(1, "secret");
        Set<RolesOrPermissionDTO> permissions = new HashSet<>();
        permissions.add(permission);

        FilterDTO.setPermissions(permissions);
        assertEquals("permissions are not equal", permissions, FilterDTO.getPermissions());
    }

    @Test
    public void testConstructor() {
        FilterDTO FilterDTO = new FilterDTO(filterID);
        assertEquals("Filter ID is not equal", filterID, FilterDTO.getId());
    }
}
