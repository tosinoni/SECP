package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.GroupCreateRequest;
import com.visucius.secp.DTO.GroupModifyRequest;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.Role;
import com.visucius.secp.models.User;
import org.junit.Before;
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
    private static PermissionDAO permissionsDAO;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final long INVALID_Permission_ID = 1000;
    private static final long INVALID_ROLE_ID = 1000;
    private static final String DUPLICATE_NAME = "duplicate";
    private static final String VALID_GROUP_NAME = "validName";
    private static final Set<Long> validPermissions = new HashSet<>();
    private static final Set<Long> validRoles = new HashSet<>();
    private static final int groupWithNoRolesID = 1;
    private static final int groupWithRolesID = 2;

    private static final long NUMBER_OF_ROLES = 5;
    private static final long NUMBER_OF_PERMISSIONS = 5;
    private static final String ROLE_NAME = "test";
    private static final String PERMISSION_NAME = "test";


    public static Group createGroup(int id, Set<Permission> permissions, Set<Role> roles)
    {
        Group group = new Group();
        group.setId(id);
        group.setPermissions(permissions);
        group.setRoles(roles);
        return group;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        userDAO = Mockito.mock(UserDAO.class);
        groupDAO = Mockito.mock(GroupDAO.class);
        rolesDAO = Mockito.mock(RolesDAO.class);
        permissionsDAO = Mockito.mock(PermissionDAO.class);


        List<User> validUsers = new ArrayList<>();
        validUsers.add(new User());

        Group validGroup = new Group(VALID_GROUP_NAME);
        validGroup.setId(1);

        Mockito.when(rolesDAO.find(
            AdditionalMatchers.not(Matchers.eq(INVALID_ROLE_ID)))).
            thenReturn(Optional.fromNullable(new Role(ROLE_NAME)));
        Mockito.when(permissionsDAO.find(
            AdditionalMatchers.not(Matchers.eq(INVALID_Permission_ID)))).
            thenReturn(Optional.fromNullable(new Permission(PERMISSION_NAME)));

        Mockito.when(permissionsDAO.find(INVALID_Permission_ID)).thenReturn(Optional.absent());
        Mockito.when(rolesDAO.find(INVALID_ROLE_ID)).thenReturn(Optional.absent());

        Mockito.when(groupDAO.findByName(DUPLICATE_NAME)).thenReturn(new Group());


        Mockito.when(groupDAO.save(Matchers.any())).thenReturn(validGroup);

        Mockito.when(userDAO.findUsersWithRole(Matchers.anyInt())).thenReturn(validUsers);
        Mockito.when(userDAO.findUsersWithPermissionLevel(Matchers.anyInt())).thenReturn(validUsers);

        Mockito.when(groupDAO.findByName(VALID_GROUP_NAME)).thenReturn(null);


        controller = new GroupController(groupDAO,userDAO,rolesDAO,permissionsDAO);
    }


    @Before
    public void setUpTests()
    {
        for(long i = 0; i<NUMBER_OF_PERMISSIONS;i++)
        {
            validPermissions.add(i);
        }

        for(long i = 0; i<NUMBER_OF_ROLES;i++)
        {
            validRoles.add(i);
        }

        Set<Role> roles = new HashSet<>();
        Set<Permission> permissions = new HashSet<>();

        for(long id : validRoles)
        {
            Role role = new Role(ROLE_NAME);
            role.setId((int)id);
            roles.add(role);
        }
        for(long id : validPermissions)
        {
            Permission permission = new Permission(PERMISSION_NAME);
            permission.setId((int)id);
            permissions.add(permission);
        }

        Group groupWithRoles = createGroup(groupWithRolesID,permissions,roles);
        Group groupWithNoRoles = createGroup(groupWithNoRolesID,permissions,new HashSet<>());
        Mockito.when(groupDAO.find(groupWithRolesID)).thenReturn(Optional.fromNullable(groupWithRoles));
        Mockito.when(groupDAO.find(groupWithNoRolesID)).thenReturn(Optional.fromNullable(groupWithNoRoles));

    }

    @Test
    public void GroupNameIsTooLongTest()
    {
        GroupCreateRequest request = new GroupCreateRequest(
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
        GroupCreateRequest request = new GroupCreateRequest(
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
        GroupCreateRequest request = new GroupCreateRequest(
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
        GroupCreateRequest request = new GroupCreateRequest(
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
        GroupCreateRequest request = new GroupCreateRequest(
            VALID_GROUP_NAME,
            permissions,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.PERMISSION_ID_INVALID, INVALID_Permission_ID));
        controller.createGroup(request);
    }

    @Test
    public void NotEnoughPermissionsTest()
    {
        Set<Long> permissions = new HashSet<>();
        GroupCreateRequest request = new GroupCreateRequest(
            VALID_GROUP_NAME,
            permissions,
            validPermissions
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_PERMISSIONS_REQUIRED);
        controller.createGroup(request);
    }

    @Test
    public void ModifyGroupTooHaveNoPermissionsTest()
    {
        GroupModifyRequest request = new GroupModifyRequest(
            validPermissions,
            new HashSet<>()
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(GroupErrorMessages.GROUP_PERMISSIONS_REQUIRED);
        controller.deletePermissions(request, groupWithRolesID);
    }

    @Test
    public void RemovePermissionThatGroupDoesNotHaveTest()
    {
        HashSet<Long> removePermissions = new HashSet<>();
        removePermissions.add(20L);
        GroupModifyRequest request = new GroupModifyRequest(
            removePermissions,
            new HashSet<>()
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.PERMISSION_ID_INVALID, 20));
        controller.deletePermissions(request, groupWithRolesID);
    }

    @Test
    public void RemoveRoleThatGroupDoesNotHaveTest()
    {
        HashSet<Long> removeRoles = new HashSet<>();
        removeRoles.add(20L);
        GroupModifyRequest request = new GroupModifyRequest(
            new HashSet<>(),
            removeRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.ROLE_ID_INVALID, 20));
        controller.deleteRoles(request, groupWithRolesID);
    }


    @Test
    public void AddInvalidRoleTest()
    {
        HashSet<Long> addRoles = new HashSet<>();
        addRoles.add(INVALID_ROLE_ID);
        GroupModifyRequest request = new GroupModifyRequest(
            new HashSet<>(),
            addRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.ROLE_ID_INVALID, INVALID_ROLE_ID));
        controller.addRolesToGroup(request, groupWithRolesID);
    }

    @Test
    public void AddInvalidPermissionTest()
    {
        HashSet<Long> addPermissions = new HashSet<>();
        addPermissions.add(INVALID_ROLE_ID);
        GroupModifyRequest request = new GroupModifyRequest(
            addPermissions,
            new HashSet<>()
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.PERMISSION_ID_INVALID, INVALID_ROLE_ID));
        controller.addPermissionsToGroup(request, groupWithRolesID);
    }

    @Test
    public void AddValidRoleTest()
    {
        HashSet<Long> addRoles = new HashSet<>();
        addRoles.add(6L);
        GroupModifyRequest request = new GroupModifyRequest(
            new HashSet<>(),
            addRoles
        );


        Response response = controller.addRolesToGroup(request, groupWithRolesID);
        Response validResponse = Response.status(Response.Status.CREATED).entity(1L).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void AddValidPermissionTest()
    {
        HashSet<Long> addPermissions = new HashSet<>();
        addPermissions.add(6L);
        GroupModifyRequest request = new GroupModifyRequest(
            addPermissions,
            new HashSet<>()
        );

        Response response = controller.addPermissionsToGroup(request, groupWithRolesID);
        Response validResponse = Response.status(Response.Status.CREATED).entity(1L).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void AddValidPermissionsTest()
    {
        HashSet<Long> addPermissions = new HashSet<>();
        for(long i = NUMBER_OF_PERMISSIONS; i<NUMBER_OF_PERMISSIONS*2;i++)
        {
            addPermissions.add(i);
        }

        GroupModifyRequest request = new GroupModifyRequest(
            addPermissions,
            new HashSet<>()
        );

        Response response = controller.addPermissionsToGroup(request, groupWithRolesID);
        Response validResponse = Response.status(Response.Status.CREATED).entity(1L).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void AddValidRolesTest()
    {
        HashSet<Long> addRoles = new HashSet<>();
        for(long i = NUMBER_OF_ROLES; i<NUMBER_OF_ROLES*2;i++)
        {
            addRoles.add(i);
        }

        GroupModifyRequest request = new GroupModifyRequest(
            new HashSet<>(),
            addRoles
        );

        Response response = controller.addRolesToGroup(request, groupWithRolesID);
        Response validResponse = Response.status(Response.Status.CREATED).entity(1L).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void RemoveValidPermissionsAndRolesTest()
    {
        HashSet<Long> removePermissions = new HashSet<>();
        for(long i = 0; i<NUMBER_OF_PERMISSIONS-1;i++)
        {
            removePermissions.add(i);
        }

        GroupModifyRequest request = new GroupModifyRequest(
            removePermissions,
            new HashSet<>()
        );

        Response response = controller.deletePermissions(request, groupWithRolesID);
        Response validResponse = Response.status(Response.Status.CREATED).entity(1L).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void RemoveValidRolesTest()
    {
        HashSet<Long> removeRoles = new HashSet<>();
        for(long i = 0; i<NUMBER_OF_ROLES;i++)
        {
            removeRoles.add(i);
        }

        GroupModifyRequest request = new GroupModifyRequest(
            new HashSet<>(),
            removeRoles
        );

        Response response = controller.deleteRoles(request, groupWithRolesID);
        Response validResponse = Response.status(Response.Status.CREATED).entity(1L).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void ModifyGroupWithEmptyRequestTest()
    {
        GroupModifyRequest request = new GroupModifyRequest();

        Response response =  controller.addRolesToGroup(request, groupWithRolesID);
        Response validResponse = Response.status(Response.Status.CREATED).entity(1L).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void ValidGroupCreationTest()
    {
        GroupCreateRequest request = new GroupCreateRequest(
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
