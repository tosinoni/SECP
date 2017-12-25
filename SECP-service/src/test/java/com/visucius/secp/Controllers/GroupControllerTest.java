package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.GroupCreationRequest;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Role;
import com.visucius.secp.models.User;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.AdditionalMatchers;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.ws.rs.WebApplicationException;
import java.util.HashSet;
import java.util.Set;

public class GroupControllerTest {

    private static GroupController controller;
    private static UserDAO userDAO;
    private static GroupDAO groupDAO;
    private static RolesDAO rolesDAO;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final int INVALID_USER_ID = 1000;
    private static final int INVALID_ROLE_ID = 1000;
    private static final String DUPLICATE_NAME = "duplicate";
    private static final String VALID_GROUP_NAME = "validName";
    private static final Set<Integer> validUsers = new HashSet<>();
    private static final Set<Integer> validRoles = new HashSet<>();



    @BeforeClass
    public static void setUp() throws Exception {
        userDAO = Mockito.mock(UserDAO.class);
        groupDAO = Mockito.mock(GroupDAO.class);
        rolesDAO = Mockito.mock(RolesDAO.class);

        validUsers.add(1);
        validUsers.add(2);
        validUsers.add(3);
        validUsers.add(4);
        validUsers.add(5);

        validRoles.add(1);
        validRoles.add(2);
        validRoles.add(3);
        validRoles.add(4);
        validRoles.add(5);

        Mockito.when(rolesDAO.find(
            AdditionalMatchers.not(Matchers.eq(INVALID_ROLE_ID)))).
            thenReturn(Optional.fromNullable(new Role()));
        Mockito.when(userDAO.find(
            AdditionalMatchers.not(Matchers.eq(INVALID_USER_ID)))).
            thenReturn(Optional.fromNullable(new User()));

        Mockito.when(userDAO.find(INVALID_USER_ID)).thenReturn(Optional.absent());
        Mockito.when(rolesDAO.find(INVALID_ROLE_ID)).thenReturn(Optional.absent());
        Mockito.when(groupDAO.findByName(DUPLICATE_NAME)).thenReturn(new Group());


        Mockito.when(groupDAO.findByName(VALID_GROUP_NAME)).thenReturn(null);


        controller = new GroupController(groupDAO,userDAO,rolesDAO);
    }

    @Test
    public void GroupNameIsTooLongTest()
    {
        GroupCreationRequest request = new GroupCreationRequest(
            "verrylongfifdsfdsfsdffdsfssafastnameamefdsafdsafsdfddfdddddsssssssssfsdfsfsdfsfsdfsdfsdffdsfsfsfsf",
            validUsers,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_NAME_INVALID);
        controller.createGroup(request);
    }

    @Test
    public void DuplicateNameTest() {
        GroupCreationRequest request = new GroupCreationRequest(
            DUPLICATE_NAME,
            validUsers,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_NAME_INVALID);
        controller.createGroup(request);
    }

    @Test
    public void GroupNameIsTooShortTest() {
        GroupCreationRequest request = new GroupCreationRequest(
            "a",
            validUsers,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_NAME_INVALID);
        controller.createGroup(request);
    }

    @Test
    public void RolesIDInvalidTest()
    {
        Set<Integer> roles = new HashSet<>();
        roles.add(INVALID_ROLE_ID);
        GroupCreationRequest request = new GroupCreationRequest(
            VALID_GROUP_NAME,
            validUsers,
            roles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.ROLE_ID_INVALID, INVALID_ROLE_ID));
        controller.createGroup(request);
    }

    @Test
    public void UserIDInvalidTest()
    {
        Set<Integer> users = new HashSet<>();
        users.add(INVALID_USER_ID);
        users.add(20);
        GroupCreationRequest request = new GroupCreationRequest(
            VALID_GROUP_NAME,
            users,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.USER_ID_INVALID, INVALID_USER_ID));
        controller.createGroup(request);
    }

    @Test
    public void NotEnoughUsersTest()
    {
        Set<Integer> users = new HashSet<>();
        GroupCreationRequest request = new GroupCreationRequest(
            VALID_GROUP_NAME,
            users,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_TOO_SMALL);
        controller.createGroup(request);
    }

    @Test
    public void ToManyUsersTest()
    {
        Set<Integer> users = new HashSet<>();

        for (int i = 1; i<= GroupController.MAXIMUM_AMOUNT_OF_USERS +2; i++)
        {
            users.add(i);
        }

        GroupCreationRequest request = new GroupCreationRequest(
            VALID_GROUP_NAME,
            users,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_TOO_BIG);
        controller.createGroup(request);
    }

}
