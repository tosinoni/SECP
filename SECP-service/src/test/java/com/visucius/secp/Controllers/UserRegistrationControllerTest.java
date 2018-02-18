package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.User.UserErrorMessage;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.DTO.RolesOrPermissionDTO;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RecordsDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class UserRegistrationControllerTest {

    private UserRegistrationController controller;
    private UserDAO userDAO;
    private PermissionDAO permissionDAO;
    private GroupDAO groupDAO;
    private RecordsDAO recordsDAO;


    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        userDAO = Mockito.mock(UserDAO.class);
        groupDAO = Mockito.mock(GroupDAO.class);
        permissionDAO = Mockito.mock(PermissionDAO.class);
        recordsDAO = Mockito.mock(RecordsDAO.class);

        Mockito.when(permissionDAO.find(Matchers.anyLong())).thenReturn(Optional.fromNullable(new Permission("level", "Blue")));
        Mockito.when(userDAO.findByEmail("duplicate@email.com")).thenReturn(new User());
        Mockito.when(userDAO.findByUserName("duplicateUsername")).thenReturn(new User());
        Mockito.when(userDAO.save(new User())).thenReturn(new User());
        controller = new UserRegistrationController(userDAO,permissionDAO,groupDAO, recordsDAO);
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

        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.FIRST_NAME_INVALID);
        Response response = controller.registerUser(new User(),request);
    }

    @Test
    public void LastNameIsTooLongTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "verrylonglastnameamefdsafdsafsdfddfdddddsssssssssfsdfsfsdfsfsdfsdfsdffdsfsfsfsf",
            "alifarah",
            "test@gmail.com",
            "Password1");
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.LAST_NAME_INVALID);
        Response response = controller.registerUser(new User(), request);
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
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.FIRST_NAME_INVALID);
        Response response = controller.registerUser(new User(),request);
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
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.LAST_NAME_INVALID);
        Response response = controller.registerUser(new User(),request);
    }

    @Test
    public void PasswordIsEmptyTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "");
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.PASSWORD_INVALID);
        Response response = controller.registerUser(new User(),request);
    }

    @Test
    public void PasswordIsTooShortTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "pass");
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.PASSWORD_INVALID);
        Response response = controller.registerUser(new User(),request);
    }

    @Test
    public void PasswordIsTooLongTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "Verylongpassword12342343243242324323");
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.PASSWORD_INVALID);
        Response response = controller.registerUser(new User(),request);
    }

    @Test
    public void PasswordIsInInvalidFormatTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "alifarah",
            "test@gmail.com",
            "password1");
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.PASSWORD_INVALID);
        Response response = controller.registerUser(new User(),request);
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
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.EMAIL_INVALID);
        Response response = controller.registerUser(new User(),request);
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

        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.EMAIL_INVALID);
        Response response = controller.registerUser(new User(),request);
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
        request.permission = new RolesOrPermissionDTO(1, "name");
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("ali");
        userDTO.setLastName("farah");
        userDTO.setLastName("alifarah");

        Response response = controller.registerUser(new User(),request);
        Response validResponse = Response.status(Response.Status.CREATED).entity(userDTO).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }
    @Test
    public void DuplicateUsernameTest() {
        UserRegistrationRequest request = new UserRegistrationRequest(
            "ali",
            "farah",
            "duplicateUsername",
            "alifarah",
            "Password!");
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.DUPLICATE_USERNAME);
        Response response = controller.registerUser(new User(),request);
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
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.DUPLICATE_EMAIL);
        Response response = controller.registerUser(new User(),request);
    }

    @Test
    public void testFindUserByInValidEmail()
    {
        boolean isValid = controller.isUsernameValid(null);
        assertFalse("user does not exist", isValid);
    }

    @Test
    public void testFindUserByValidEmail()
    {
        String email = "joh@doe.com";
        Mockito.when(userDAO.findByEmail(email)).thenReturn(new User("johnDoe", email ));
        boolean isValid = controller.isEmailValid(email);
        assertTrue("Email does not exist", isValid);
    }

    @Test
    public void testFindUserByInValidUserName()
    {
        boolean isValid = controller.isEmailValid(null);
        assertFalse("user does not exist", isValid);
    }

    @Test
    public void testFindUserByValidUserName()
    {
        String username = "johnDoe";
        Mockito.when(userDAO.findByUserName(username)).thenReturn(new User(username, "joh@doe.com" ));
        boolean isValid = controller.isUsernameValid(username);
        assertTrue("Email is not equal", isValid);
    }
}
