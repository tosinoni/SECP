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
    public void testDisplayName(){
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setDisplayName("user1");
        assertEquals("Display Name is not equal", "user1", userDTO.getDisplayName());
    }

    @Test
    public void testAvatarURL(){
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setAvatar_url("mxc://matrix.org/wefh34uihSDRGhw34");
        assertEquals("Avatar URL is not equal", "mxc://matrix.org/wefh34uihSDRGhw34", userDTO.getAvatar_url());
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
