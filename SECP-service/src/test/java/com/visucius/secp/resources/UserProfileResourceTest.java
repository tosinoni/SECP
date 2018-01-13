package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.User.UserProfileController;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.User;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class UserProfileResourceTest {
    private static final String getUserProfileUrl = "/user/profile/id/";

    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private UserProfileController userProfileController = new UserProfileController(userDAO);


    private static final long userID = 1;

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new UserProfileResource(userProfileController))
        .build();

    @Test
    public void testGetProfileWithInvalidID()
    {
        //testing getProfile with null id
        Response response = resources.client().target(getUserProfileUrl + null).request().get();
        ResponseValidator.validate(response, 204);

        //testing getProfile with empty id
        response = resources.client().target(getUserProfileUrl + " ").request().get();
        ResponseValidator.validate(response, 204);

        //testing getProfile with letters as id
        response = resources.client().target(getUserProfileUrl + "abc").request().get();
        ResponseValidator.validate(response, 204);

        //test getProfile with invalid id
        long id = 1;
        Optional<User> user = Optional.absent();
        Mockito.when(userDAO.find(id)).thenReturn(user);
        response = resources.client().target(getUserProfileUrl + id).request().get();
        ResponseValidator.validate(response,400);
    }

    @Test
    public void testGetProfileWithValidID(){
        long id = 12;
        User mockedUser = new User();
        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(getUserProfileUrl + id).request().get();
        ResponseValidator.validate(response, 200);
    }
}
