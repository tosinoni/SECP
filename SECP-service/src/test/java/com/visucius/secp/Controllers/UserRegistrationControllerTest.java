package com.visucius.secp.Controllers;

import com.visucius.secp.Controllers.User.UserErrorMessage;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class UserRegistrationControllerTest {

    private UserRegistrationController controller;
    private UserDAO userDAO;

    @Before
    public void setUp() throws Exception {
        userDAO = Mockito.mock(UserDAO.class);
        Mockito.when(userDAO.findByEmail("duplicate@email.com")).thenReturn(new User());
        Mockito.when(userDAO.findByUserName("duplicateUsername")).thenReturn(new User());
        Mockito.when(userDAO.save(new User())).thenReturn(new User());
        controller = new UserRegistrationController(userDAO);
    }

    @Test
    public void FirstNameIsTooLongTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "verrylongfirstnameamefdsafdsafsdfddfdddddsssssssssfsdfsfsdfsfsdfsdfsdffdsfsfsfsf",
            "farah",
            "alifarah",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.FIRST_NAME_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void LastNameIsTooLongTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "verrylonglastnameamefdsafdsafsdfddfdddddsssssssssfsdfsfsdfsfsdfsdfsdffdsfsfsfsf",
            "alifarah",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.LAST_NAME_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void FirstNameIsEmptyTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "",
            "farah",
            "alifarah",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.FIRST_NAME_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void LastNameIsEmptyTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "",
            "alifarah",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.LAST_NAME_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void PasswordIsEmptyTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.PASSWORD_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void PasswordIsTooShortTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "pass");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.PASSWORD_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void PasswordIsTooLongTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "Verylongpassword12342343243242324323");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.PASSWORD_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void PasswordIsInInvalidFormatTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "password1");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.PASSWORD_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void EmailIsInInvalidFormatTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "testgmail.com",
            "Password1");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.EMAIL_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void EmailIsEmptyTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "",
            "Password!");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.EMAIL_INVALID));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void ValidUserTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.registerUser(request);

        assertTrue(response.success);
        assertEquals(response.status, Response.Status.CREATED);
        assertTrue(response.errors.isEmpty());
        assertTrue(response.getUserID() == 0);
        assertEquals(response.getMessage(), UserErrorMessage.USER_CREATED);
    }
    @Test
    public void DuplicateUsernameTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "duplicateUsername",
            "alifarah",
            "Password!");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.DUPLICATE_USERNAME));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void DuplicateEmailTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "duplicate@email.com",
            "Password!");
        UserRegistrationResponse response = controller.registerUser(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserErrorMessage.DUPLICATE_EMAIL));
        assertEquals(response.getMessage(), UserErrorMessage.USER_NOT_CREATED);
    }

    @Test
    public void testFindUserByInValidEmail()
    {
        User user = controller.findUserByEmail(null);
        assertEquals("user does not exist", null, user);
    }

    @Test
    public void testFindUserByValidEmail()
    {
        String email = "joh@doe.com";
        Mockito.when(userDAO.findByEmail(email)).thenReturn(new User("johnDoe", email ));
        User user = controller.findUserByEmail(email);
        assertNotNull("user does not exists", user);
        assertEquals("Email is not equal", email, user.getEmail());
    }

    @Test
    public void testFindUserByInValidUserName()
    {
        User user = controller.findUserByUsername(null);
        assertEquals("user does not exist", null, user);
    }

    @Test
    public void testFindUserByValidUserName()
    {
        String username = "johnDoe";
        Mockito.when(userDAO.findByUserName(username)).thenReturn(new User(username, "joh@doe.com" ));
        User user = controller.findUserByUsername(username);
        assertNotNull("user does not exists", user);
        assertEquals("Email is not equal", username, user.getUsername());
    }
}
