package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.User.DeviceController;
import com.visucius.secp.DTO.SecretDTO;
import com.visucius.secp.daos.DeviceDAO;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.Device;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Secret;
import com.visucius.secp.models.User;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

public class DeviceResourceTest {

    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private GroupDAO groupDAO = Mockito.mock(GroupDAO.class);
    private DeviceDAO deviceDAO = Mockito.mock(DeviceDAO.class);

    private DeviceController deviceController = new DeviceController(userDAO, groupDAO, deviceDAO);

    private static final String deviceUrl = "/device/";
    private static final String deviceForUserUrl = deviceUrl + "user/";
    private static final String deviceForGroupUrl = deviceUrl + "group/";
    private static final String addSecretToDevicesUrl = deviceUrl + "secret/";


    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new DeviceResource(deviceController))
        .build();

    @Test
    public void testGetSecretKeysForDevice() {
        String deviceName = "device1";

        //testing with no device name
        Response response = resources.client().target(deviceUrl + " " + " /secret").request().get();
        ResponseValidator.validate(response, 204);

        //testing with invalid device name
        response = resources.client().target(deviceUrl + deviceName + " /secret").request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testGetDevicesForUser() {
        long userId = 1;
        //testing with invalid user id
        Mockito.when(userDAO.find(userId)).thenReturn(Optional.absent());
        Response response = resources.client().target(deviceForUserUrl + userId).request().get();
        ResponseValidator.validate(response, 204);

        //testing with valid user id but with no device
        Mockito.when(userDAO.find(userId)).thenReturn(Optional.of(new User()));
        response = resources.client().target(deviceForUserUrl + userId).request().get();
        ResponseValidator.validate(response, 204);

        //testing with valid user id and device
        Device device = new Device("device1", "key");
        User user = new User();
        user.getDevices().add(device);
        Mockito.when(userDAO.find(userId)).thenReturn(Optional.of(user));
        response = resources.client().target(deviceForUserUrl + userId).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testGetDevicesForGroup() {
        long groupId = 1;

        //testing with invalid group id
        Mockito.when(groupDAO.find(groupId)).thenReturn(Optional.absent());
        Response response = resources.client().target(deviceForGroupUrl + groupId).request().get();
        ResponseValidator.validate(response, 204);

        //testing with valid group id but with no devices
        Mockito.when(groupDAO.find(groupId)).thenReturn(Optional.of(new Group()));
        response = resources.client().target(deviceForGroupUrl + groupId).request().get();
        ResponseValidator.validate(response, 204);

        //testing with valid group id and device
        Device device = new Device("device1", "key");
        User user = new User();
        user.getDevices().add(device);

        Group group = new Group();
        group.getUsers().add(user);
        Mockito.when(groupDAO.find(groupId)).thenReturn(Optional.of(group));
        response = resources.client().target(deviceForGroupUrl + groupId).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testaddSecretKeyForDevices() {
        //testing with null secret dtos
        Response response = resources.client().target(addSecretToDevicesUrl).request().post(null);
        ResponseValidator.validate(response, 400);

        //testing with empty secret dto set
        Set<SecretDTO> secretDTOS = new HashSet<>();
        response = resources.client().target(addSecretToDevicesUrl).request().post(Entity.json(secretDTOS));
        ResponseValidator.validate(response, 400);

        //testing with null secret dto in set
        secretDTOS.add(null);
        response = resources.client().target(addSecretToDevicesUrl).request().post(Entity.json(secretDTOS));
        ResponseValidator.validate(response, 400);
        secretDTOS.remove(null);

        //testing with invalid group id
        SecretDTO secretDTO = new SecretDTO();
        secretDTOS.add(secretDTO);
        Mockito.when(groupDAO.find(secretDTO.getGroupID())).thenReturn(Optional.absent());
        response = resources.client().target(addSecretToDevicesUrl).request().post(Entity.json(secretDTOS));
        ResponseValidator.validate(response, 400);

        //testing with invalid user id
        Mockito.when(groupDAO.find(secretDTO.getGroupID())).thenReturn(Optional.of(new Group()));
        Mockito.when(userDAO.find(secretDTO.getUserID())).thenReturn(Optional.absent());
        response = resources.client().target(addSecretToDevicesUrl).request().post(Entity.json(secretDTOS));
        ResponseValidator.validate(response, 400);

        //testing with invalid device id
        Mockito.when(userDAO.find(secretDTO.getUserID())).thenReturn(Optional.of(new User()));
        Mockito.when(deviceDAO.find(secretDTO.getDeviceID())).thenReturn(Optional.absent());
        response = resources.client().target(addSecretToDevicesUrl).request().post(Entity.json(secretDTOS));
        ResponseValidator.validate(response, 400);

        //testing with no secret
        Mockito.when(userDAO.find(secretDTO.getUserID())).thenReturn(Optional.of(new User()));
        Mockito.when(deviceDAO.find(secretDTO.getDeviceID())).thenReturn(Optional.of(new Device()));
        response = resources.client().target(addSecretToDevicesUrl).request().post(Entity.json(secretDTOS));
        ResponseValidator.validate(response, 400);

        //testing with valid parameters
        secretDTOS.remove(secretDTO);
        secretDTO.setEncryptedSecret("encryptedText");
        secretDTOS.add(secretDTO);
        Secret secret = new Secret();
        Mockito.when(deviceDAO.findSecretByDeviceAndGroupID(secretDTO.getGroupID(), secretDTO.getDeviceID()))
            .thenReturn(secret);
        response = resources.client().target(addSecretToDevicesUrl).request().post(Entity.json(secretDTOS));
        ResponseValidator.validate(response, 200);
    }
}
