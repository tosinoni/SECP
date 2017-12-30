package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.User.UserController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.DTO.DeviceDTO;
import com.visucius.secp.daos.DeviceDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.Device;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Arrays;

public class UserResourceTest {
    private static final String isUserAnAdminUrl = "/user/verify/admin/id/";
    private static final String verifyEmailUrl = "/user/verify/email";
    private static final String verifyUsernameUrl = "/user/verify/username";
    private static final String addDeviceUrl = "/user/device/";
    private static final String defaultUrl = "/user/id/";


    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private DeviceDAO deviceDAO = Mockito.mock(DeviceDAO.class);
    private UserController userController = new UserController(userDAO, deviceDAO);
    private UserRegistrationController userRegistrationController  = Mockito.mock((UserRegistrationController.class));

    //test variables
    private static final long userID = 1;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new UserResource(userController, userRegistrationController))
        .build();
    @Test
    public void testIsUserAnAdminWithNullID()
    {
        Response response = resources.client().target(isUserAnAdminUrl + null).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testIsUserAnAdminWithEmptyStringID()
    {
        Response response = resources.client().target(isUserAnAdminUrl + " ").request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testIsUserAnAdminWithLettersAsID()
    {
        Response response = resources.client().target(isUserAnAdminUrl + "abc").request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testIsUserAnAdminWithInvalidID()
    {
        long id = 12;
        Optional<User> user = Optional.of(new User());
        Mockito.when(userDAO.find(id)).thenReturn(user);
        Response response = resources.client().target(isUserAnAdminUrl + id).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testIsUserAnAdminWithValidID()
    {
        long id = 12;
        User mockedUser = new User();
        mockedUser.setLoginRole(LoginRole.ADMIN);

        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(isUserAnAdminUrl + id).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testVerifyWithInValidEmail()
    {
        Response response = resources.client().target(verifyEmailUrl + "/invalid").request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testVerifyWithValidEmail()
    {
        String email = "joh@doe.com";
        Mockito.when(userRegistrationController.isEmailValid(email)).thenReturn(true);
        Response response = resources.client().target(verifyEmailUrl + "/" + email).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testVerifyWithValidUserName()
    {
        String username = "johnDoe";
        Mockito.when(userRegistrationController.isUsernameValid(username)).thenReturn(true);
        Response response = resources.client().target(verifyUsernameUrl + "/" + username).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testVerifyWithInValidUserName()
    {
        Response response = resources.client().target(verifyUsernameUrl + "/invalid").request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testIsDeviceRegisteredWithNoDeviceName()
    {
        String url = defaultUrl + 1 + "/device/name/ ";
        Response response = resources.client().target(url).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testIsDeviceRegisteredForUserWithNoDevice()
    {
        String url = defaultUrl + 1 + "/device/name/name";
        Response response = resources.client().target(url).request().get();
        ResponseValidator.validate(response, 204);

        Mockito.when(deviceDAO.getDevicesForUser(1)).thenReturn(Arrays.asList());
        response = resources.client().target(url).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testIsDeviceRegisteredForUserWithDevice()
    {
        String url = defaultUrl + 1 + "/device/name/name";
        DeviceDTO deviceDTO = getDefaultDevice();
        Device device = new Device(deviceDTO.getDeviceName(), deviceDTO.getPublicKey());

        Mockito.when(deviceDAO.getDevicesForUser(1)).thenReturn(Arrays.asList(device));
        Mockito.when(deviceDAO.findByDeviceName("name")).thenReturn(device);

        Response response = resources.client().target(url).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testAddDeviceWithEmptyInformation()
    {
        Response response = resources.client().target(addDeviceUrl).request().post(null);
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testAddDeviceWithNoDeviceNameAndPublicKey()
    {
        DeviceDTO deviceDTO = new DeviceDTO();
        Response response = resources.client().target(addDeviceUrl).request().post(Entity.json(deviceDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testAddDeviceWithNoDeviceName()
    {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setPublicKey("key1");
        Response response = resources.client().target(addDeviceUrl).request().post(Entity.json(deviceDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testAddDeviceWithNoPublicKey()
    {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceName("name");
        Response response = resources.client().target(addDeviceUrl).request().post(Entity.json(deviceDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testAddDeviceWithInvalidUserID()
    {
        DeviceDTO deviceDTO = getDefaultDevice();

        Optional<User> user = Optional.absent();
        Mockito.when(userDAO.find(1)).thenReturn(user);

        Response response = resources.client().target(addDeviceUrl).request().post(Entity.json(deviceDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testAddExistingDevice()
    {
        DeviceDTO deviceDTO = getDefaultDevice();
        Device device = new Device(deviceDTO.getDeviceName(), deviceDTO.getPublicKey());

        Optional<User> user = Optional.of(new User());
        Mockito.when(userDAO.find(1)).thenReturn(user);
        Mockito.when(deviceDAO.findByDeviceName(deviceDTO.getDeviceName())).thenReturn(device);
        Mockito.when(deviceDAO.getDevicesForUser(1)).thenReturn(Arrays.asList(device));

        Response response = resources.client().target(addDeviceUrl).request().post(Entity.json(deviceDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testAddDeviceWithValidInputs()
    {
        DeviceDTO deviceDTO = getDefaultDevice();

        Optional<User> user = Optional.of(new User());
        Mockito.when(userDAO.find(1)).thenReturn(user);

        Response response = resources.client().target(addDeviceUrl).request().post(Entity.json(deviceDTO));
        ResponseValidator.validate(response, 201);
    }

    @Test
    public void testGetPublicKeysWithInvalidUserID()
    {
        String url = defaultUrl + 1 + "/publicKey";
        Optional<User> user = Optional.absent();
        Mockito.when(userDAO.find(1)).thenReturn(user);

        Response response = resources.client().target(url).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testGetPublicKeysForUserWithNoDevices()
    {
        String url = defaultUrl + userID + "/publicKey";
        Optional<User> user = Optional.of(new User());
        Mockito.when(userDAO.find(userID)).thenReturn(user);
        Mockito.when(deviceDAO.getDevicesForUser(userID)).thenReturn(Arrays.asList());

        Response response = resources.client().target(url).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testGetPublicKeysForADeviceWithNoPublicKey()
    {
        String url = defaultUrl + userID + "/publicKey";
        Device device = new Device();
        device.setId(1);

        Optional<User> user = Optional.of(new User());
        Mockito.when(userDAO.find(userID)).thenReturn(user);
        Mockito.when(deviceDAO.getDevicesForUser(userID)).thenReturn(Arrays.asList(device));

        Response response = resources.client().target(url).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testGetPublicKeysForUsersDevicesValidInput()
    {
        String url = defaultUrl + userID + "/publicKey";
        Device device = new Device();
        device.setId(1);
        device.setPublicKey("key");

        Optional<User> user = Optional.of(new User());
        Mockito.when(userDAO.find(userID)).thenReturn(user);
        Mockito.when(deviceDAO.getDevicesForUser(userID)).thenReturn(Arrays.asList(device));

        Response response = resources.client().target(url).request().get();
        ResponseValidator.validate(response, 200);
    }

    private DeviceDTO getDefaultDevice() {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceName("name");
        deviceDTO.setPublicKey("key");
        deviceDTO.setUserID(1);

        return deviceDTO;
    }
}
