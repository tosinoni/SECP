package com.visucius.secp.UseCase;

import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class UserRegistrationControllerTest {

    private UserRegistrationController controller;

    @Before
    public void setUp() throws Exception {
        controller = new UserRegistrationController();
    }

    @Test
    public void FirstNameIsTooLongTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "verrylongfirstnameamefdsafdsafsdfddfdddddsssssssssfsdfsfsdfsfsdfsdfsdffdsfsfsfsf",
            "farah",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.FIRST_NAME_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void LastNameIsTooLongTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "verrylonglastnameamefdsafdsafsdfddfdddddsssssssssfsdfsfsdfsfsdfsdfsdffdsfsfsfsf",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.LAST_NAME_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void FirstNameIsEmptyTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "",
            "farah",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.FIRST_NAME_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void LastNameIsEmptyTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.LAST_NAME_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void PasswordIsEmptyTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "test@gmail.com",
            "");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.PASSWORD_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void PasswordIsTooShortTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "test@gmail.com",
            "pass");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.PASSWORD_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void PasswordIsTooLongTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "test@gmail.com",
            "Verylongpassword12342343243242324323");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.PASSWORD_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void PasswordIsInInvalidFormatTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "test@gmail.com",
            "password1");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.PASSWORD_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void EmailIsInInvalidFormatTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "testgmail.com",
            "Password1");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.EMAIL_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void EmailIsEmptyTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "",
            "Password!");
        UserRegistrationResponse response = controller.handle(request);

        assertFalse(response.success);
        assertEquals(response.status, Response.Status.BAD_REQUEST);
        assertTrue(response.errors.contains(UserRegistrationController.EMAIL_INVALID));
        assertEquals(response.message, UserRegistrationController.USER_NOT_CREATED);
    }

    @Test
    public void ValidUserTest()
    {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "test@gmail.com",
            "Password1");
        UserRegistrationResponse response = controller.handle(request);

        assertTrue(response.success);
        assertEquals(response.status, Response.Status.CREATED);
        assertTrue(response.errors.isEmpty());
        assertEquals(response.message, UserRegistrationController.USER_CREATED);
    }

}
