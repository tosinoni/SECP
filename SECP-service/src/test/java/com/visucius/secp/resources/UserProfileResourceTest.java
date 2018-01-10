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

    @Test
    public void testGetDisplayNameWithInvalidID(){

        //Testing getDisplayName with null ID
        Response response = resources.client().target(getUserProfileUrl + null + "/displayname").request().get();
        ResponseValidator.validate(response, 204);

        //Testing getDisplayName with empty ID
        response = resources.client().target(getUserProfileUrl + " " + "/displayname").request().get();
        ResponseValidator.validate(response, 204);

        //Testing getDisplayName with letters ID
        response = resources.client().target(getUserProfileUrl + "abc" + "/displayname").request().get();
        ResponseValidator.validate(response, 204);

        //test getDisplayName with invalid id
        long id = 1;
        Optional<User> user = Optional.absent();
        Mockito.when(userDAO.find(id)).thenReturn(user);
        response = resources.client().target(getUserProfileUrl + id + "/displayname").request().get();
        ResponseValidator.validate(response,204);
    }

    @Test
    public void testGetDisplayNameWithValidID(){
        long id = 12;
        User mockedUser = new User();
        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(getUserProfileUrl + id + "/displayname").request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testSetDisplayNameWithInvalidInputs(){

        //Testing with null
        Response response = resources.client().target(getUserProfileUrl + 1 + "/displayname").request().post(null);
        ResponseValidator.validate(response, 400);

        //Testing with no display name
        UserDTO userDTO = new UserDTO(userID);
        response = resources.client().target(getUserProfileUrl + " " + "/displayname").request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);

        //userDTO.setDisplayName("testdisplayname");
        response = resources.client().target(getUserProfileUrl + 1 + "/displayname").request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testGetAvatarURLWithInvalidID(){

        //Testing getDisplayname with null ID
        Response response = resources.client().target(getUserProfileUrl + null + "/avatar_url").request().get();
        ResponseValidator.validate(response, 204);

        //Testing getDisplayname with empty ID
        response = resources.client().target(getUserProfileUrl + " " + "/avatar_url").request().get();
        ResponseValidator.validate(response, 204);

        //Testing getDisplayname with letters ID
        response = resources.client().target(getUserProfileUrl + "abc" + "/avatar_url").request().get();
        ResponseValidator.validate(response, 204);

        //Testing getDisplayname with invalid id
        long id = 1;
        Optional<User> user = Optional.absent();
        Mockito.when(userDAO.find(id)).thenReturn(user);
        response = resources.client().target(getUserProfileUrl + id + "/avatar_url").request().get();
        ResponseValidator.validate(response,204);
    }

    @Test
    public void testSetAvatarURLWithInvalidInputs(){

        //Testing with null
        Response response = resources.client().target(getUserProfileUrl + 1 + "/avatar_url").request().post(null);
        ResponseValidator.validate(response, 400);

        //Testing with no display name
        UserDTO userDTO = new UserDTO(userID);
        response = resources.client().target(getUserProfileUrl + " " + "/avatar_url").request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);

        //userDTO.setAvatar_url("https://test.com");
        response = resources.client().target(getUserProfileUrl + 1 + "/avatar_url").request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);
    }


    /*Some problems with this currently
    @Test
    public void testGetAvatarURLWithValidID(){
        long id = 12;
        User mockedUser = new User();
        Optional<User> user = Optional.of(mockedUser);
        Mockito.when(userDAO.find(id)).thenReturn(user);

        Response response = resources.client().target(getUserProfileUrl + id + "/avatar_url").request().get();
        ResponseValidator.validate(response, 200);
    }*/

}
