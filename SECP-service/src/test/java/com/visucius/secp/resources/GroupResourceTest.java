package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.GroupController;
import com.visucius.secp.Controllers.User.UserProfileController;
import com.visucius.secp.daos.*;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.User;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.Arrays;

public class GroupResourceTest {

    private static final String groupsUrl = "/groups";
    private static final String getGroupUrl = "/groups/id/";


    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private GroupDAO groupDAO = Mockito.mock(GroupDAO.class);
    private RolesDAO rolesDAO = Mockito.mock(RolesDAO.class);
    private PermissionDAO permissionDAO = Mockito.mock(PermissionDAO.class);
    private UserProfileController userProfileController = Mockito.mock(UserProfileController.class);

    private GroupController groupController  = new GroupController(groupDAO, userDAO, rolesDAO, permissionDAO, userProfileController);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
        .addProvider((new AuthValueFactoryProvider.Binder<>(User.class)))
        .addResource(new GroupResource(groupController))
        .build();

    @Test
    public void testGetAllGroups() {
        //testing empty groups in db
        Response response = resources.client().target(groupsUrl).request().get();
        ResponseValidator.validate(response, 204);

        //testing with roles in db
        Group group = new Group("developer");
        Mockito.when(groupDAO.findAllPublicGroups()).thenReturn(Arrays.asList(group));
        response = resources.client().target(groupsUrl).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testDeleteGroups() {

        long id = 12;
        Group mockedGroup = new Group("developer");

        Mockito.when(groupDAO.find(id)).thenReturn(Optional.fromNullable(mockedGroup));
        Response response = resources.client().target(groupsUrl + "/" + 12).request().delete();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testGetGroupWithInvalidGroupId() {
        //testing get groups with no group id
        Response response = resources.client().target(getGroupUrl + null).request().get();
        ResponseValidator.validate(response, 204);

        //testing get groups with empty group id
        response = resources.client().target(getGroupUrl + " ").request().get();
        ResponseValidator.validate(response, 204);

        //testing get groups with letters as id
        response = resources.client().target(getGroupUrl + "abc").request().get();
        ResponseValidator.validate(response, 204);

        //testing get groups with invalid id
        long id = 1;
        Optional<Group> group = Optional.absent();
        Mockito.when(groupDAO.find(id)).thenReturn(group);
        response = resources.client().target(getGroupUrl + id).request().get();
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testGetGroupWithValidGroupId() {
        long id = 12;
        Group mockedGroup = new Group("developer");

        Optional<Group> group = Optional.of(mockedGroup);
        Mockito.when(groupDAO.find(id)).thenReturn(group);

        Response response = resources.client().target(getGroupUrl + id).request().get();
        ResponseValidator.validate(response, 200);
    }
}
