package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.GroupCreationRequest;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.PermissionLevelDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.PermissionLevel;
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
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;


public class GroupControllerTest {

    private static GroupController controller;
    private static UserDAO userDAO;
    private static GroupDAO groupDAO;
    private static RolesDAO rolesDAO;
    private static PermissionLevelDAO permissionsDAO;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final long INVALID_Permission_ID = 1000;
    private static final long INVALID_ROLE_ID = 1000;
    private static final String DUPLICATE_NAME = "duplicate";
    private static final String VALID_GROUP_NAME = "validName";
    private static final Set<Long> validPermissions = new HashSet<>();
    private static final Set<Long> validRoles = new HashSet<>();

    @BeforeClass
    public static void setUp() throws Exception {
        userDAO = Mockito.mock(UserDAO.class);
        groupDAO = Mockito.mock(GroupDAO.class);
        rolesDAO = Mockito.mock(RolesDAO.class);
        permissionsDAO = Mockito.mock(PermissionLevelDAO.class);

        validPermissions.add(1L);
        validPermissions.add(2L);
        validPermissions.add(3L);
        validPermissions.add(4L);
        validPermissions.add(5L);

        validRoles.add(1L);
        validRoles.add(2L);
        validRoles.add(3L);
        validRoles.add(4L);
        validRoles.add(5L);

        List<User> validUsers = new ArrayList<>();
        validUsers.add(new User());
        Group validGroup = new Group(VALID_GROUP_NAME);
        validGroup.setId(1);

        Mockito.when(rolesDAO.find(
            AdditionalMatchers.not(Matchers.eq(INVALID_ROLE_ID)))).
            thenReturn(Optional.fromNullable(new Role("roleName")));
        Mockito.when(permissionsDAO.find(
            AdditionalMatchers.not(Matchers.eq(INVALID_Permission_ID)))).
            thenReturn(Optional.fromNullable(new PermissionLevel()));

        Mockito.when(permissionsDAO.find(INVALID_Permission_ID)).thenReturn(Optional.absent());
        Mockito.when(rolesDAO.find(INVALID_ROLE_ID)).thenReturn(Optional.absent());
        Mockito.when(groupDAO.findByName(DUPLICATE_NAME)).thenReturn(new Group());
        Mockito.when(groupDAO.save(Matchers.any())).thenReturn(validGroup);
        Mockito.when(userDAO.findUsersWithRole(Matchers.anyInt())).thenReturn(validUsers);

        Mockito.when(groupDAO.findByName(VALID_GROUP_NAME)).thenReturn(null);


        controller = new GroupController(groupDAO,userDAO,rolesDAO,permissionsDAO);
    }

    @Test
    public void GroupNameIsTooLongTest()
    {
        GroupCreationRequest request = new GroupCreationRequest(
            "verrylongfifdsfdsfsdffdsfssafastnameamefdsafdsafsdfddfdddddsssssssssfsdfsfsdfsfsdfsdfsdffdsfsfsfsf",
            validPermissions,
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
            validPermissions,
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
            validPermissions,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_NAME_INVALID);
        controller.createGroup(request);
    }

    @Test
    public void RolesIDInvalidTest()
    {
        Set<Long> roles = new HashSet<>();
        roles.add(INVALID_ROLE_ID);
        GroupCreationRequest request = new GroupCreationRequest(
            VALID_GROUP_NAME,
            validPermissions,
            roles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.ROLE_ID_INVALID, INVALID_ROLE_ID));
        controller.createGroup(request);
    }

    @Test
    public void PermissionIDInvalidTest()
    {
        Set<Long> permissions = new HashSet<>();
        permissions.add(INVALID_Permission_ID);
        permissions.add(20L);
        GroupCreationRequest request = new GroupCreationRequest(
            VALID_GROUP_NAME,
            permissions,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.Permission_ID_INVALID, INVALID_Permission_ID));
        controller.createGroup(request);
    }

    @Test
    public void NotEnoughRolesTest()
    {
        Set<Long> roles = new HashSet<>();
        GroupCreationRequest request = new GroupCreationRequest(
            VALID_GROUP_NAME,
            validPermissions,
            roles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_ROLES_REQUIRED);
        controller.createGroup(request);
    }

    @Test
    public void ValidGroupCreationTest()
    {
        GroupCreationRequest request = new GroupCreationRequest(
            VALID_GROUP_NAME,
            validPermissions,
            validRoles
        );

        Response response = controller.createGroup(request);
        Response validResponse = Response.status(Response.Status.CREATED).entity(1L).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }
}
