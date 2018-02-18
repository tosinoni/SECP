package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.User.UserErrorMessage;
import com.visucius.secp.Controllers.User.UserProfileController;
import com.visucius.secp.DTO.*;
import com.visucius.secp.daos.*;
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
    private static RecordsDAO recordsDAO;

    private static UserProfileController userProfileController;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final long INVALID_Permission_ID = 1000;
    private static final long INVALID_ROLE_ID = 1000;
    private static final String DUPLICATE_NAME = "duplicate";
    private static final String VALID_GROUP_NAME = "validName";
    private static final String VALID_ROLE_NAME = "role";
    private static final String VALID_PERMISSION_NAME = "permission";


    private static final Set<RolesOrPermissionDTO> validPermissions = new HashSet<>();
    private static final Set<RolesOrPermissionDTO> validRoles = new HashSet<>();
    private static final int groupWithNoRolesID = 1;
    private static final int groupWithRolesID = 2;

    private static final long NUMBER_OF_ROLES = 5;
    private static final long NUMBER_OF_PERMISSIONS = 5;
    private static final String ROLE_NAME = "test";
    private static final String PERMISSION_NAME = "test";
    private static final long GROUPID = 1L;


    private static Group createGroup(int id, Set<Permission> permissions, Set<Role> roles)
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
        recordsDAO = Mockito.mock(RecordsDAO.class);
        userProfileController = Mockito.mock(UserProfileController.class);

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


        controller = new GroupController(groupDAO,userDAO,rolesDAO,permissionsDAO, recordsDAO, userProfileController);
    }


    @Before
    public void setUpTests()
    {
        for(long i = 0; i<NUMBER_OF_PERMISSIONS;i++)
        {
            validPermissions.add(new RolesOrPermissionDTO(i,"permission"));
        }

        for(long i = 0; i<NUMBER_OF_ROLES;i++)
        {
            validRoles.add(new RolesOrPermissionDTO(i, "role"));
        }

        Set<Role> roles = new HashSet<>();
        Set<Permission> permissions = new HashSet<>();

        for(RolesOrPermissionDTO id : validRoles)
        {
            Role role = new Role(ROLE_NAME);
            role.setId((int)id.getId());
            roles.add(role);
        }
        for(RolesOrPermissionDTO id : validPermissions)
        {
            Permission permission = new Permission(PERMISSION_NAME);
            permission.setId((int)id.getId());
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
        controller.createPublicGroup(new User(), request);
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
        controller.createPublicGroup(new User(),request);
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
        controller.createPublicGroup(new User(),request);
    }

    @Test
    public void RolesIDInvalidTest()
    {
        Set<RolesOrPermissionDTO> roles = new HashSet<>();
        roles.add(new RolesOrPermissionDTO(INVALID_ROLE_ID, ""));
        GroupCreateRequest request = new GroupCreateRequest(
            VALID_GROUP_NAME,
            validPermissions,
            roles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.ROLE_ID_INVALID, INVALID_ROLE_ID));
        controller.createPublicGroup(new User(),request);
    }

    @Test
    public void PermissionIDInvalidTest()
    {
        Set<RolesOrPermissionDTO> permissions = new HashSet<>();
        permissions.add(new RolesOrPermissionDTO(INVALID_Permission_ID, ""));
        GroupCreateRequest request = new GroupCreateRequest(
            VALID_GROUP_NAME,
            permissions,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.PERMISSION_ID_INVALID, INVALID_Permission_ID));
        controller.createPublicGroup(new User(),request);
    }

    @Test
    public void AddInvalidRoleTest()
    {
        HashSet<RolesOrPermissionDTO> addRoles = new HashSet<>();
        addRoles.add(new RolesOrPermissionDTO(INVALID_ROLE_ID,ROLE_NAME));
        GroupDTO groupDTO = new GroupDTO(GROUPID);
        groupDTO.setRoles(addRoles);
        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.ROLE_ID_INVALID, INVALID_ROLE_ID));
        controller.modifyGroup(new User(),groupDTO);
    }

    @Test
    public void AddInvalidPermissionTest()
    {
        HashSet<RolesOrPermissionDTO> addPermissions = new HashSet<>();
        addPermissions.add(new RolesOrPermissionDTO(INVALID_ROLE_ID,PERMISSION_NAME));
        GroupDTO groupDTO = new GroupDTO(GROUPID);
        groupDTO.setPermissions(addPermissions);

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.PERMISSION_ID_INVALID, INVALID_ROLE_ID));
        controller.modifyGroup(new User(),groupDTO);
    }

    @Test
    public void AddValidRoleTest()
    {
        HashSet<RolesOrPermissionDTO> addRoles = new HashSet<>();
        addRoles.add(new RolesOrPermissionDTO(8L,ROLE_NAME));
        GroupDTO groupDTO = new GroupDTO(GROUPID);
        groupDTO.setRoles(addRoles);
        groupDTO.setPermissions(new HashSet<>());

        Response response = controller.modifyGroup(new User(),groupDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getGroupResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void AddValidPermissionTest()
    {
        HashSet<RolesOrPermissionDTO> addPermissions = new HashSet<>();
        addPermissions.add(new RolesOrPermissionDTO(6L,PERMISSION_NAME));
        GroupDTO groupDTO = new GroupDTO(GROUPID);
        groupDTO.setPermissions(addPermissions);
        groupDTO.setRoles(new HashSet<>());

        Response response = controller.modifyGroup(new User(),groupDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getGroupResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }


    @Test
    public void AddValidPermissionsAndRolesTest()
    {
        HashSet<RolesOrPermissionDTO> addPermissions = new HashSet<>();
        for(long i = NUMBER_OF_PERMISSIONS; i<NUMBER_OF_PERMISSIONS*2;i++)
        {
            addPermissions.add(new RolesOrPermissionDTO(i,PERMISSION_NAME + i));
        }

        HashSet<RolesOrPermissionDTO> addRoles = new HashSet<>();
        for(long i = NUMBER_OF_ROLES; i<NUMBER_OF_ROLES*2;i++)
        {
            addPermissions.add(new RolesOrPermissionDTO(i,ROLE_NAME + i));
        }

        GroupDTO groupDTO = new GroupDTO(GROUPID);
        groupDTO.setPermissions(addPermissions);
        groupDTO.setRoles(addRoles);

        Response response = controller.modifyGroup(new User(),groupDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getGroupResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void RemoveValidPermissionsAndRolesTest()
    {
        HashSet<RolesOrPermissionDTO> removePermissions = new HashSet<>();
        for(long i = 0; i<NUMBER_OF_PERMISSIONS-1;i++)
        {
            removePermissions.add(new RolesOrPermissionDTO(i,PERMISSION_NAME + i));
        }

        HashSet<RolesOrPermissionDTO> removeRoles = new HashSet<>();
        for(long i = 0; i<NUMBER_OF_ROLES-1;i++)
        {
            removeRoles.add(new RolesOrPermissionDTO(i,ROLE_NAME + i));
        }

        GroupDTO groupDTO = new GroupDTO(GROUPID);
        groupDTO.setPermissions(removePermissions);
        groupDTO.setRoles(removeRoles);

        Response response = controller.modifyGroup(new User(),groupDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getGroupResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void ModifyGroupWithEmptyRequestTest()
    {
        GroupDTO groupDTO = new GroupDTO(GROUPID);

        Response response =  controller.modifyGroup(new User(),groupDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getGroupResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }


    @Test
    public void createPrivateThatExistsGroup()
    {
        Group group = new Group();
        group.setId(GROUPID);
        UserDTO userDTO = new UserDTO(1);
        userDTO.setUsername("user1");
        User user = new User();
        user.setId(1);
        user.setUsername("user2");
        List<Group> groups = new ArrayList<>();
        groups.add(group);
        Mockito.when(groupDAO.findPrivateGroupForUsers(Matchers.anySet())).thenReturn(groups);
        Mockito.when(userDAO.getUserWithDevices(Matchers.anyLong())).thenReturn(Optional.fromNullable( new User()));
        Response response =  controller.createPrivateGroup(user,userDTO);
        Response validResponse = Response.status(Response.Status.OK).entity(getGroupResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void createPrivateGroupWithInvalidUser()
    {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("user1");
        User user = new User();
        user.setId(1);
        user.setUsername("user2");
        Mockito.when(groupDAO.findPrivateGroupForUsers(Matchers.anySet())).thenReturn(new ArrayList());
        Mockito.when(userDAO.getUserWithDevices(Matchers.anyLong())).thenReturn(Optional.fromNullable(null));
        exception.expect(WebApplicationException.class);
        exception.expectMessage(UserErrorMessage.USER_ID_INVALID);
        controller.createPrivateGroup(user,userDTO);
    }


    @Test
    public void createPrivateGroup()
    {
        UserDTO userDTO = new UserDTO(1);
        userDTO.setUsername("user1");
        User user = new User();
        user.setId(1);
        user.setUsername("user2");
        Mockito.when(userDAO.getUserWithDevices(Matchers.anyLong())).thenReturn(Optional.fromNullable( new User()));
        Mockito.when(groupDAO.findPrivateGroupForUsers(Matchers.anySet())).thenReturn(new ArrayList());
        Response response =  controller.createPrivateGroup(user,userDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getGroupResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void validGroupCreationTest()
    {
        GroupCreateRequest request = new GroupCreateRequest(
            VALID_GROUP_NAME,
            validPermissions,
            validRoles
        );

        Response response = controller.createPublicGroup(new User(),request);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getGroupResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void testGetGroupsForUsers()
    {
        User user = new User();
        Group group = new Group();
        group.setId(GROUPID);
        Set<Group> groups = new HashSet<>();
        Set<GroupDTO> groupDTOS = new HashSet<>();
        groupDTOS.add(getGroupResponse());
        groups.add(group);
        user.setGroups(groups);
        Mockito.when(userDAO.getUserWithGroups(Matchers.anyLong())).thenReturn(Optional.fromNullable(user));
        Response response = controller.getGroupsForUser(user);
        Response validResponse = Response.status(Response.Status.OK).entity(groupDTOS).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    private GroupDTO getGroupResponse () {
        GroupDTO groupDTO = new GroupDTO(GROUPID);
        return groupDTO;
    }
}
