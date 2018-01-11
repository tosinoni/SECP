package com.visucius.secp.DTO;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GroupDTOTest {
    private final long groupID = 1;


    @Test
    public void testGroupID() {
        GroupDTO groupDTO = new GroupDTO(groupID);
        groupDTO.setGroupID(groupID);
        assertEquals("group id is not equal",groupID, groupDTO.getGroupID());
    }

    @Test
    public void testUsers() {
        GroupDTO groupDTO = new GroupDTO(groupID);

        UserDTO userDTO = new UserDTO(1);
        Set<UserDTO> users = new HashSet<>();
        users.add(userDTO);

        groupDTO.setUsers(users);
        assertEquals("users are is not equal", users, groupDTO.getUsers());
    }

    @Test
    public void testName() {
        GroupDTO groupDTO = new GroupDTO(groupID);
        groupDTO.setName("developer");
        assertEquals("name is not equal", "developer", groupDTO.getName());
    }

    @Test
    public void testNumberOfRoles() {
        GroupDTO groupDTO = new GroupDTO(groupID);
        groupDTO.setNumOfRoles(3);
        assertEquals("name is not equal", 3, groupDTO.getNumOfRoles());
    }

    @Test
    public void testNumberOfPermissions() {
        GroupDTO groupDTO = new GroupDTO(groupID);
        groupDTO.setNumOfPermissions(3);
        assertEquals("name is not equal", 3, groupDTO.getNumOfPermissions());
    }

    @Test
    public void testNumberOfUsers() {
        GroupDTO groupDTO = new GroupDTO(groupID);
        groupDTO.setNumOfUsers(3);
        assertEquals("name is not equal", 3, groupDTO.getNumOfUsers());
    }

    @Test
    public void testRoles() {
        GroupDTO groupDTO = new GroupDTO(groupID);

        RolesOrPermissionDTO role = new RolesOrPermissionDTO(1, "tester");
        Set<RolesOrPermissionDTO> roles = new HashSet<>();
        roles.add(role);

        groupDTO.setRoles(roles);
        assertEquals("roles are not equal", roles, groupDTO.getRoles());
    }

    @Test
    public void testPermissions() {
        GroupDTO groupDTO = new GroupDTO(groupID);

        RolesOrPermissionDTO permission = new RolesOrPermissionDTO(1, "secret");
        Set<RolesOrPermissionDTO> permissions = new HashSet<>();
        permissions.add(permission);

        groupDTO.setPermissions(permissions);
        assertEquals("roles are not equal", permissions, groupDTO.getPermissions());
    }

    @Test
    public void testConstructor() {
        GroupDTO groupDTO = new GroupDTO(groupID);
        assertEquals("groupD is not equal",groupID, groupDTO.getGroupID());
    }
}
