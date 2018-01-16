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
    private static final String modifyProfileUrl = "/user/profile/modify";

    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private UserProfileController userProfileController = new UserProfileController(userDAO);

    //Test variables
    private static final long userID = 1;
    private static final String displayName = "testdisplayname";
    private static final String avatarUrl = "https://user-images.githubusercontent.com/14824913/34922743-f386cabc-f961-11e7-84af-be3f61f41005.png";

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new UserProfileResource(userProfileController))
        .build();

    @Test
    public void testModifyUserProfileWithInvalidDisplayName(){
        UserDTO userDTO = createUser();

        //Test modify profile with invalid display name
        userDTO.setDisplayName("");

        User mockedUser = new User();
        Optional<User> userFromDB = Optional.of(mockedUser);
        Mockito.when(userDAO.find(userID)).thenReturn(userFromDB);
        Response response = resources.client().target(modifyProfileUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testModifyUserProfileWithInvalidAvatarUrl(){
        UserDTO userDTO = createUser();

        //Test modify profile with invalid avatar Url
        userDTO.setAvatarUrl("");

        User mockedUser = new User();
        Optional<User> userFromDB = Optional.of(mockedUser);
        Mockito.when(userDAO.find(userID)).thenReturn(userFromDB);
        Response response = resources.client().target(modifyProfileUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testModifyUserProfileWithValidParamaters(){
        UserDTO userDTO = createUser();

        User mockedUser = new User();
        mockedUser.setId(1);

        Optional<User> userFromDB = Optional.of(mockedUser);
        Mockito.when(userDAO.find(userID)).thenReturn(userFromDB);
        Response response = resources.client().target(modifyProfileUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testModifyUserProfileWithInvalidUserID(){
        UserDTO userDTO = createUser();

        Optional<User> userFromDB = Optional.absent();
        Mockito.when(userDAO.find(userID)).thenReturn(userFromDB);
        Response response = resources.client().target(modifyProfileUrl).request().post(Entity.json(userDTO));
        ResponseValidator.validate(response, 204);
    }

    public UserDTO createUser() {
        UserDTO userDTO = new UserDTO(userID);
        userDTO.setDisplayName(displayName);
        userDTO.setAvatarUrl(avatarUrl);

        return userDTO;
    }
}
