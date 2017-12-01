package com.visucius.secp.resources;

import com.visucius.secp.DTO.LoginRequestDTO;
import com.visucius.secp.Controllers.User.LoginRequestController;
import com.visucius.secp.Controllers.TokenController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.User;
import com.visucius.secp.util.PasswordUtil;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class EntryResourceTest {

    private static final String url = "/login";
    private static final String verifyUrl = "/verify";
    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private TokenController tokenController = Mockito.mock((TokenController.class));
    private UserRegistrationController userRegistrationController  = Mockito.mock((UserRegistrationController.class));
    private LoginRequestController loginRequestController = new LoginRequestController(tokenController, userDAO);


    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new EntryResource(userRegistrationController, loginRequestController))
        .build();

    @Test
    public void testLoginWithNoArgument() {
        Response response = resources.client().target(url).request().post(null);
        ResponseValidator.validate(response, 400, "Login Failed. Please provide your username and password.");
    }

    @Test
    public void testLoginWithEmptyUserNameAndPasswordString() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("", "");
        Response response = resources.client().target(url).request().post(Entity.json(loginRequestDTO));
        ResponseValidator.validate(response, 400, "Login Failed. Please provide your username.");
    }

    @Test
    public void testLoginWithNoUsername() {
        LoginRequestDTO loginRequestDTO = createLoginInfo();
        loginRequestDTO.setUsername(null);

        Response response = resources.client().target(url).request().post(Entity.json(loginRequestDTO));
        ResponseValidator.validate(response, 400, "Login Failed. Please provide your username.");
    }

    @Test
    public void testLoginWithNoPassword() {
        LoginRequestDTO loginRequestDTO = createLoginInfo();
        loginRequestDTO.setPassword(null);

        Response response = resources.client().target(url).request().post(Entity.json(loginRequestDTO));
        ResponseValidator.validate(response, 400, "Login Failed. Please provide your password.");
    }

    @Test
    public void testLoginWithInvalidUsername() {
        LoginRequestDTO loginRequestDTO = createLoginInfo();

        Mockito.when(userDAO.findByUserName("username")).thenReturn(null);

        Response response = resources.client().target(url).request().post(Entity.json(loginRequestDTO));
        ResponseValidator.validate(response, 401, "Login Failed. username does not exist.");
    }

    @Test
    public void testLoginWithInvalidPassword() throws Exception {
        LoginRequestDTO loginRequestDTO = createLoginInfo();

        User mockedUser = new User();
        mockedUser.setUsername("username");
        mockedUser.setPassword(PasswordUtil.createHash("12345"));
        Mockito.when(userDAO.findByUserName("username")).thenReturn(mockedUser);

        Response response = resources.client().target(url).request().post(Entity.json(loginRequestDTO));
        ResponseValidator.validate(response, 401, "Login Failed. Incorrect password.");
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequestDTO loginRequestDTO = createLoginInfo();

        User mockedUser = new User();
        mockedUser.setUsername("username");
        mockedUser.setPassword(PasswordUtil.createHash("password"));
        Mockito.when(userDAO.findByUserName("username")).thenReturn(mockedUser);
        Mockito.when(tokenController.createTokenFromUsername("username")).thenReturn("token12345");

        Response response = resources.client().target(url).request().post(Entity.json(loginRequestDTO));

        ResponseValidator.validate(response, 200);
        ResponseValidator.validate(response, "token", "token12345");
        ResponseValidator.validate(response, "loginRole", "NORMAL");
    }

    @Test
    public void testVerifyWithInValidEmail()
    {
        Response response = resources.client().target(verifyUrl + "?email=" + null).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testVerifyWithValidEmail()
    {
        String email = "joh@doe.com";
        Mockito.when(userRegistrationController.findUserByEmail(email)).thenReturn(new User("johnDoe", email ));
        Response response = resources.client().target(verifyUrl + "?email=" + email).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testVerifyWithInValidUserName()
    {
        Response response = resources.client().target(verifyUrl + "?username=" + null).request().get();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testVerifyWithValidUserName()
    {
        String username = "johnDoe";
        Mockito.when(userRegistrationController.findUserByUsername(username)).thenReturn(new User(username, "joh@doe.com" ));
        Response response = resources.client().target(verifyUrl + "?username=" + username).request().get();
        ResponseValidator.validate(response, 200);
    }
    private LoginRequestDTO createLoginInfo() {
        return new LoginRequestDTO("username", "password");
    }
}
