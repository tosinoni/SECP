package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.Admin.AdminController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.DTO.AppCreateDTO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.Role;
import com.visucius.secp.models.User;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AdminResourceTest {

    private static final String adminUrl = "/admin/";
    private static final String rolesUrl = "/admin/roles";
    private static final String permissionsUrl = "/admin/permissions";
    private static final String deleteRolesUrl = "/admin/role/id/";
    private static final String deletePermissionsUrl = "/admin/permission/id/";

    private static final String roleName = "tester";
    private static final String permissionName = "TOP_SECRET";
    private static final long roleId = 1;
    private static final long permissionId = 1;


    private UserDAO userDAO = Mockito.mock(UserDAO.class);
    private RolesDAO rolesDAO = Mockito.mock(RolesDAO.class);
    private PermissionDAO permissionDAO = Mockito.mock(PermissionDAO.class);

    private AdminController adminController = new AdminController(userDAO, rolesDAO, permissionDAO);
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

    @Test
    public void testAddRolesWithInvalidRequestObject()
    {
        Response response = resources.client().target(rolesUrl).request().post(null);
        ResponseValidator.validate(response, 400);

        //testing with empty roles
        AppCreateDTO request = new AppCreateDTO();
        response = resources.client().target(rolesUrl).request().post(Entity.json(request));
        ResponseValidator.validate(response, 400);

        //testing with invalid roleName
        Set<String> roles = new HashSet<>();
        roles.add(roleName);
        request.setRoles(roles);

        Mockito.when(rolesDAO.findByName(roleName)).thenReturn(new Role(roleName));
        response = resources.client().target(rolesUrl).request().post(Entity.json(request));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testAddRolesWithValidRequestObject()
    {
        AppCreateDTO request = new AppCreateDTO();

        Set<String> roles = new HashSet<>();
        roles.add(roleName);
        request.setRoles(roles);

        Role role = new Role(roleName);
        Mockito.when(rolesDAO.save(role)).thenReturn(role);

        Response response = resources.client().target(rolesUrl).request().post(Entity.json(request));
        ResponseValidator.validate(response, 201);
    }

    @Test
    public void testAddPermissionsWithInvalidRequestObject()
    {
        Response response = resources.client().target(permissionsUrl).request().post(null);
        ResponseValidator.validate(response, 400);

        //testing with empty roles
        AppCreateDTO request = new AppCreateDTO();
        response = resources.client().target(permissionsUrl).request().post(Entity.json(request));
        ResponseValidator.validate(response, 400);

        //testing with invalid roleName
        Set<String> permissions = new HashSet<>();
        permissions.add(permissionName);
        request.setPermissions(permissions);

        Mockito.when(permissionDAO.findByName(permissionName)).thenReturn(new Permission(permissionName));
        response = resources.client().target(permissionsUrl).request().post(Entity.json(request));
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testAddPermissionsWithValidRequestObject()
    {
        AppCreateDTO request = new AppCreateDTO();

        Set<String> permissions = new HashSet<>();
        permissions.add(permissionName);
        request.setPermissions(permissions);

        Permission permission = new Permission(permissionName);
        Mockito.when(permissionDAO.save(permission)).thenReturn(permission);

        Response response = resources.client().target(permissionsUrl).request().post(Entity.json(request));
        ResponseValidator.validate(response, 201);
    }

    @Test
    public void testDeleteRolesWithInvalidID()
    {
        Response response = resources.client().target(deleteRolesUrl + null).request().delete();
        ResponseValidator.validate(response, 400);

        response = resources.client().target(deleteRolesUrl + " ").request().delete();
        ResponseValidator.validate(response, 400);

        response = resources.client().target(deleteRolesUrl + "abc").request().delete();
        ResponseValidator.validate(response, 400);

        Optional<Role> role = Optional.absent();
        Mockito.when(rolesDAO.find(roleId)).thenReturn(role);

        response = resources.client().target(deleteRolesUrl + roleId).request().delete();
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testDeleteRolesWithAValidID()
    {
        Role role = new Role(roleName);

        Optional<Role> dbRole = Optional.of(role);
        Mockito.when(rolesDAO.find(roleId)).thenReturn(dbRole);

        Response response = resources.client().target(deleteRolesUrl + roleId).request().delete();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testDeletePermissionsWithInvalidID()
    {
        Response response = resources.client().target(deletePermissionsUrl + null).request().delete();
        ResponseValidator.validate(response, 400);

        response = resources.client().target(deletePermissionsUrl + " ").request().delete();
        ResponseValidator.validate(response, 400);

        response = resources.client().target(deletePermissionsUrl + "abc").request().delete();
        ResponseValidator.validate(response, 400);

        Optional<Permission> permission = Optional.absent();
        Mockito.when(permissionDAO.find(permissionId)).thenReturn(permission);

        response = resources.client().target(deletePermissionsUrl + permissionId).request().delete();
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testDeletePermissionsWithAValidID()
    {
        Permission permission = new Permission(permissionName);

        Optional<Permission> dbPermission = Optional.of(permission);
        Mockito.when(permissionDAO.find(permissionId)).thenReturn(dbPermission);

        Response response = resources.client().target(deletePermissionsUrl + permissionId).request().delete();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testGetAllRoles() {
        //testing empty roles in db
        Response response = resources.client().target(rolesUrl).request().get();
        ResponseValidator.validate(response, 204);

        //testing with roles in db
        Role role = new Role(roleName);
        Mockito.when(rolesDAO.findAll()).thenReturn(Arrays.asList(role));
        response = resources.client().target(rolesUrl).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testGetAllPermissions() {
        //testing empty roles in db
        Response response = resources.client().target(permissionsUrl).request().get();
        ResponseValidator.validate(response, 204);

        //testing with roles in db
        Permission permission = new Permission(permissionName);
        Mockito.when(permissionDAO.findAll()).thenReturn(Arrays.asList(permission));
        response = resources.client().target(permissionsUrl).request().get();
        ResponseValidator.validate(response, 200);
    }
}
