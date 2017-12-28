package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.User.UserController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.daos.DeviceDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

public class UserResourceTest {
    private static final String isUserAnAdminUrl = "/user/verify/admin/id/";
    private static final String verifyEmailUrl = "/user/verify/email";
    private static final String verifyUsernameUrl = "/user/verify/username";


    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private DeviceDAO deviceDAO = Mockito.mock(DeviceDAO.class);
    private UserController userController = new UserController(userDAO, deviceDAO);
    private UserRegistrationController userRegistrationController  = Mockito.mock((UserRegistrationController.class));

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
}
