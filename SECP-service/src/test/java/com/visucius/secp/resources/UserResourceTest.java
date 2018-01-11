package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.User.UserController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.DTO.DeviceDTO;
import com.visucius.secp.DTO.RolesOrPermissionDTO;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.daos.*;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.*;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserResourceTest {
    private static final String isUserAnAdminUrl = "/user/verify/admin/id/";
    private static final String verifyEmailUrl = "/user/verify/email";
    private static final String verifyUsernameUrl = "/user/verify/username";
    private static final String addDeviceUrl = "/user/device/";
    private static final String defaultUrl = "/user/id/";
    private static final String usersUrl = "/user";
    private static final String modifyUrl = "/user/modify";


    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private DeviceDAO deviceDAO = Mockito.mock(DeviceDAO.class);
    private PermissionDAO permissionDAO = Mockito.mock(PermissionDAO.class);
    private RolesDAO rolesDAO = Mockito.mock(RolesDAO.class);
    private GroupDAO groupDAO = Mockito.mock(GroupDAO.class);

    private UserController userController = new UserController(userDAO, deviceDAO, permissionDAO, rolesDAO, groupDAO);
    private UserRegistrationController userRegistrationController  = Mockito.mock((UserRegistrationController.class));

    //test variables
    private static final long userID = 1;
    private static final String permission = "TOP_SECRET";
    private static final String role = "tester";

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
    public void testAddDeviceWithInvalidPublicKey()
    {
        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceName("name");
        deviceDTO.setPublicKey("pubkey");
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
        deviceDTO.setPublicKey(StringUtils.leftPad("key", 100, '1'));
        deviceDTO.setUserID(1);

        return deviceDTO;
    }

    @Test
    public void testGetAllUsers() {
        //testing empty groups in db
        Response response = resources.client().target(usersUrl).request().get();
        ResponseValidator.validate(response, 204);

        //testing with users in db
        User user = new User();
        Mockito.when(userDAO.findAll()).thenReturn(Arrays.asList(user));
        response = resources.client().target(usersUrl).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testGetUserWithInvalidUserId() {
        //testing get groups with no user id
        Response response = resources.client().target(defaultUrl + null).request().get();
        ResponseValidator.validate(response, 204);

        //testing get groups with empty user id
        response = resources.client().target(defaultUrl + " ").request().get();
        ResponseValidator.validate(response, 204);

        //testing get users with letters as id
        response = resources.client().target(defaultUrl + "abc").request().get();
        ResponseValidator.validate(response, 204);

        //testing get users with invalid id
        long id = 1;
        Optional<User> user = Optional.absent();
        Mockito.when(userDAO.find(id)).thenReturn(user);
        response = resources.client().target(defaultUrl + id).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testDeleteUser() {

        long id = 12;
        User user = new User();

        Mockito.when(userDAO.find(id)).thenReturn(Optional.fromNullable(user));
        Response response = resources.client().target(defaultUrl  + 12).request().delete();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testGetUserWithValidUserId() {
        long id = 12;
        User mockedUser = new User();

        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(defaultUrl + id).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testModifyUserWithInvalidPermission()
    {
        //test user modify with no userDTO
        Response response = resources.client().target(modifyUrl).request().post(null);
        ResponseValidator.validate(response, 400);

        //test user modify with no permission
        UserDTO userDTO = new UserDTO();
        response = resources.client().target(modifyUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);

        //test user modify with invalid Permission
        userDTO.setPermission(new RolesOrPermissionDTO(1, permission));
        Optional<Permission> permissionFromDB = Optional.absent();
        Mockito.when(permissionDAO.find(1)).thenReturn(permissionFromDB);
        response = resources.client().target(modifyUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testModifyUserWithInvalidRole()
    {
        //test user modify with invalid roles
        UserDTO userDTO = createUser();
        Permission permission = new Permission(userDTO.getPermission().getName());
        permission.setId(1);

        Optional<Permission> permissionFromDB = Optional.of(permission);
        Mockito.when(permissionDAO.find(1)).thenReturn(permissionFromDB);
        Optional<Role> roleFromDB = Optional.absent();
        Mockito.when(rolesDAO.find(1)).thenReturn(roleFromDB);
        Response response = resources.client().target(modifyUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testModifyUserWithInvalidUserID()
    {
        //test user modify with invalid id
        UserDTO userDTO = createUser();
        Permission permission = new Permission(userDTO.getPermission().getName());
        permission.setId(1);

        Role roleObj = new Role(role);
        roleObj.setId(1);

        Optional<Permission> permissionFromDB = Optional.of(permission);
        Mockito.when(permissionDAO.find(1)).thenReturn(permissionFromDB);
        Optional<Role> roleFromDB = Optional.of(roleObj);
        Mockito.when(rolesDAO.find(1)).thenReturn(roleFromDB);

        Optional<User> userFromDB = Optional.absent();
        Mockito.when(userDAO.find(userID)).thenReturn(userFromDB);
        Response response = resources.client().target(modifyUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testModifyUserWithValidParameters()
    {
        //test user modify with invalid id
        UserDTO userDTO = createUser();
        Permission permission = new Permission(userDTO.getPermission().getName());
        permission.setId(1);

        Role roleObj = new Role(role);
        roleObj.setId(1);

        User user = new User();
        user.setId(1);

        Optional<Permission> permissionFromDB = Optional.of(permission);
        Mockito.when(permissionDAO.find(1)).thenReturn(permissionFromDB);
        Optional<Role> roleFromDB = Optional.of(roleObj);
        Mockito.when(rolesDAO.find(1)).thenReturn(roleFromDB);
        Optional<User> userFromDB = Optional.of(user);
        Mockito.when(userDAO.find(userID)).thenReturn(userFromDB);

        Response response = resources.client().target(modifyUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 200);
    }

    public UserDTO createUser() {
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setPermission(new RolesOrPermissionDTO(1, permission));

        Set<RolesOrPermissionDTO> roles = new HashSet<>();
        roles.add(new RolesOrPermissionDTO(1, role));
        userDTO.setRoles(roles);

        return userDTO;
    }
}
