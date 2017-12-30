package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.Admin.AdminController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

public class AdminResourceTest {

    private static final String adminUrl = "/admin/";

    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private AdminController adminController = new AdminController(userDAO);
    private UserRegistrationController userRegistrationController = new UserRegistrationController(userDAO);


    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new AdminResource(adminController, userRegistrationController))
        .build();

    @Test
    public void testRegisterAdminWithInvalidStringID()
    {
        Response response = resources.client().target(adminUrl + null).request().post(null);
        ResponseValidator.validate(response, 400);

        response = resources.client().target(adminUrl + " ").request().post(null);
        ResponseValidator.validate(response, 400);

        response = resources.client().target(adminUrl + "abc").request().post(null);
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testRegisterAdminWithAnAdminID()
    {
        long id = 12;
        User mockedUser = new User();
        mockedUser.setLoginRole(LoginRole.ADMIN);

        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(adminUrl + id).request().post(null);
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testRegisterAdminWithAValidID()
    {
        long id = 12;
        User mockedUser = new User();
        mockedUser.setLoginRole(LoginRole.NORMAL);

        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(adminUrl + id).request().post(null);
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testDeleteAdminWithInvalidStringID()
    {
        Response response = resources.client().target(adminUrl + null).request().delete();
        ResponseValidator.validate(response, 400);

        response = resources.client().target(adminUrl + " ").request().delete();
        ResponseValidator.validate(response, 400);

        response = resources.client().target(adminUrl + "abc").request().delete();
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testDeleteAdminWithoutAnAdminID()
    {
        long id = 12;
        User mockedUser = new User();
        mockedUser.setLoginRole(LoginRole.NORMAL);

        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(adminUrl + id).request().delete();
        ResponseValidator.validate(response, 204);
    }

    @Test
    public void testDeleteAdminWithAValidID()
    {
        long id = 12;
        User mockedUser = new User();
        mockedUser.setLoginRole(LoginRole.ADMIN);

        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(adminUrl + id).request().delete();
        ResponseValidator.validate(response, 200);
    }
}
