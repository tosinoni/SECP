package com.visucius.secp.DTO;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DeviceDTOTest {
    private final String deviceName = "name";
    private final String publicKey = "key";
    private final long userID = 1;
    private final long deviceId = 1;


    @Test
    public void testDeviceName() {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceName(deviceName);
        assertEquals("device name is not equal",deviceName, deviceDTO.getDeviceName());
    }

    @Test
    public void testPublicKey() {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setPublicKey(publicKey);
        assertEquals("public key is not equal",publicKey, deviceDTO.getPublicKey());
    }

    @Test
    public void testUserID() {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setUserID(userID);
        assertEquals("userID is not equal",userID, deviceDTO.getUserID());
    }

    @Test
    public void testDeviceId() {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceID(1);
        assertEquals("deviceId is not equal",deviceId, deviceDTO.getDeviceID());
    }

    @Test
    public void testConstructor() {
        DeviceDTO deviceDTO = new DeviceDTO(userID, deviceName, publicKey);
        assertEquals("userID is not equal",userID, deviceDTO.getUserID());
        assertEquals("public key is not equal",publicKey, deviceDTO.getPublicKey());
        assertEquals("device name is not equal",deviceName, deviceDTO.getDeviceName());
    }

    @Test
    public void testConstructorWithDeviceIdandPublicKey() {
        DeviceDTO deviceDTO = new DeviceDTO(deviceId, publicKey);
        assertEquals("userID is not equal",deviceId, deviceDTO.getDeviceID());
        assertEquals("public key is not equal",publicKey, deviceDTO.getPublicKey());
    }
}
