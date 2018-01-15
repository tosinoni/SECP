package com.visucius.secp.DTO;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UserDTOTest {
    private final long userID = 1;

    @Test
    public void testUserID() {
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setUserID(userID);
        assertEquals("user id is not equal", userID, userDTO.getUserID());
    }

    @Test
    public void testUserName() {
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setUsername("jsmith");
        assertEquals("username is not equal", "jsmith", userDTO.getUsername());
    }

    @Test
    public void testFirstname() {
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setFirstName("John");
        assertEquals("first name is not equal", "John", userDTO.getFirstName());
    }

    @Test
    public void testLastname() {
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setLastName("Smith");
        assertEquals("last name is not equal", "Smith", userDTO.getLastName());
    }

    @Test
    public void testNumberOfRoles() {
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setNumOfRoles(3);
        assertEquals("number of roles is not equal", 3, userDTO.getNumOfRoles());
    }

    @Test
    public void testNumberOfGroups() {
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setNumOfGroups(3);
        assertEquals("number of groups is not equal", 3, userDTO.getNumOfGroups());
    }

    @Test
    public void testRoles() {
        UserDTO userDTO = new UserDTO(userID);

        RolesOrPermissionDTO role = new RolesOrPermissionDTO(1, "tester");
        Set<RolesOrPermissionDTO> roles = new HashSet<>();
        roles.add(role);

        userDTO.setRoles(roles);
        assertEquals("roles are not equal", roles, userDTO.getRoles());
    }

    @Test
    public void testPermission() {
        UserDTO userDTO = new UserDTO(userID);

        RolesOrPermissionDTO permission = new RolesOrPermissionDTO(1, "secret");

        userDTO.setPermission(permission);
        assertEquals("permission id are not equal", 1, userDTO.getPermission().getId());
        assertEquals("permission name are not equal", "secret", userDTO.getPermission().getName());
    }

    @Test
    public void testGroups() {
        UserDTO userDTO = new UserDTO(userID);
        GroupDTO groupDTO = new GroupDTO(1);

        Set<GroupDTO> groups = new HashSet<>();
        groups.add(groupDTO);

        userDTO.setGroups(groups);
        assertEquals("groups are not equal", groups, userDTO.getGroups());
    }

    @Test
    public void testDisplayName(){
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setDisplayName("user1");
        assertEquals("Display Name is not equal", "user1", userDTO.getDisplayName());
    }

    @Test
    public void testAvatarURL(){
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setAvatarUrl("mxc://matrix.org/wefh34uihSDRGhw34");
        assertEquals("Avatar URL is not equal", "mxc://matrix.org/wefh34uihSDRGhw34", userDTO.getAvatarUrl());
    }

    @Test
    public void testDevices() {
        UserDTO userDTO = new UserDTO(userID);

        DeviceDTO deviceDTO = new DeviceDTO(1, "key");
        Set<DeviceDTO> devices = new HashSet<>();
        devices.add(deviceDTO);

        userDTO.setDevices(devices);
        assertEquals("devices are not equal", devices, userDTO.getDevices());
    }

    @Test
    public void testConstructor() {
        DeviceDTO deviceDTO = new DeviceDTO(1, "key");
        Set<DeviceDTO> devices = new HashSet<>();
        devices.add(deviceDTO);

        UserDTO userDTO = new UserDTO(userID, devices);
        assertEquals("userID is not equal", userID, userDTO.getUserID());
        assertEquals("devices are not equal", devices, userDTO.getDevices());
    }
}
