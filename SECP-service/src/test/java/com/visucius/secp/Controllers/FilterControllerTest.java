package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.FilterCreateRequest;
import com.visucius.secp.DTO.FilterDTO;
import com.visucius.secp.DTO.RolesOrPermissionDTO;
import com.visucius.secp.daos.FilterDAO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.models.Filter;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.Role;
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
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;


public class FilterControllerTest {

    private static FilterController controller;
    private static FilterDAO filterDAO;
    private static RolesDAO rolesDAO;
    private static PermissionDAO permissionsDAO;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final long INVALID_Permission_ID = 1000;
    private static final long INVALID_ROLE_ID = 1000;
    private static final String DUPLICATE_NAME = "duplicate";
    private static final String VALID_FILTER_NAME = "validName";
    private static final String VALID_ROLE_NAME = "role";
    private static final String VALID_PERMISSION_NAME = "permission";


    private static final Set<RolesOrPermissionDTO> validPermissions = new HashSet<>();
    private static final Set<RolesOrPermissionDTO> validRoles = new HashSet<>();
    private static final int filterWithNoRolesID = 1;
    private static final int filterWithRolesID = 2;

    private static final long NUMBER_OF_ROLES = 5;
    private static final long NUMBER_OF_PERMISSIONS = 5;
    private static final String ROLE_NAME = "test";
    private static final String PERMISSION_NAME = "test";
    private static final long FILTERID = 1L;


    private static Filter createFilter(int id, Set<Permission> permissions, Set<Role> roles)
    {
        Filter filter = new Filter();
        filter.setId(id);
        filter.setPermissions(permissions);
        filter.setRoles(roles);
        return filter;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        filterDAO = Mockito.mock(FilterDAO.class);
        rolesDAO = Mockito.mock(RolesDAO.class);
        permissionsDAO = Mockito.mock(PermissionDAO.class);

        Filter validFilter = new Filter(VALID_FILTER_NAME);
        validFilter.setId(1);

        Mockito.when(rolesDAO.find(
            AdditionalMatchers.not(Matchers.eq(INVALID_ROLE_ID)))).
            thenReturn(Optional.fromNullable(new Role(ROLE_NAME)));
        Mockito.when(permissionsDAO.find(
            AdditionalMatchers.not(Matchers.eq(INVALID_Permission_ID)))).
            thenReturn(Optional.fromNullable(new Permission(PERMISSION_NAME)));

        Mockito.when(permissionsDAO.find(INVALID_Permission_ID)).thenReturn(Optional.absent());
        Mockito.when(rolesDAO.find(INVALID_ROLE_ID)).thenReturn(Optional.absent());

        Mockito.when(filterDAO.findByName(DUPLICATE_NAME)).thenReturn(new Filter());

        Mockito.when(filterDAO.save(Matchers.any())).thenReturn(validFilter);

        Mockito.when(filterDAO.findByName(VALID_FILTER_NAME)).thenReturn(null);

        controller = new FilterController(filterDAO,rolesDAO,permissionsDAO);
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

        Filter filterWithRoles = createFilter(filterWithRolesID,permissions,roles);
        Filter filterWithNoRoles = createFilter(filterWithNoRolesID,permissions,new HashSet<>());
        Mockito.when(filterDAO.find(filterWithRolesID)).thenReturn(Optional.fromNullable(filterWithRoles));
        Mockito.when(filterDAO.find(filterWithNoRolesID)).thenReturn(Optional.fromNullable(filterWithNoRoles));

    }

    @Test
    public void FilterNameIsTooLongTest()
    {
        FilterCreateRequest request = new FilterCreateRequest(
            "thelongestwordicanthinkofispneumonoultramicroscopicsilicovolcanoconiosis",
            validPermissions,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(FilterErrorMessages.FILTER_NAME_INVALID);
        controller.updateOrCreateFilter(request);
    }

    @Test
    public void DuplicateNameTest() {
        FilterCreateRequest request = new FilterCreateRequest(
            DUPLICATE_NAME,
            validPermissions,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(FilterErrorMessages.FILTER_NAME_INVALID);
        controller.updateOrCreateFilter(request);
    }

    @Test
    public void FilterNameIsTooShortTest() {
        FilterCreateRequest request = new FilterCreateRequest(
            "i",
            validPermissions,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(FilterErrorMessages.FILTER_NAME_INVALID);
        controller.updateOrCreateFilter(request);
    }

    @Test
    public void RolesIDInvalidTest()
    {
        Set<RolesOrPermissionDTO> roles = new HashSet<>();
        roles.add(new RolesOrPermissionDTO(INVALID_ROLE_ID, ""));
        FilterCreateRequest request = new FilterCreateRequest(
            VALID_FILTER_NAME,
            validPermissions,
            roles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(FilterErrorMessages.ROLE_ID_INVALID, INVALID_ROLE_ID));
        controller.updateOrCreateFilter(request);
    }

    @Test
    public void PermissionIDInvalidTest()
    {
        Set<RolesOrPermissionDTO> permissions = new HashSet<>();
        permissions.add(new RolesOrPermissionDTO(INVALID_Permission_ID, ""));
        FilterCreateRequest request = new FilterCreateRequest(
            VALID_FILTER_NAME,
            permissions,
            validRoles
        );

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(FilterErrorMessages.PERMISSION_ID_INVALID, INVALID_Permission_ID));
        controller.updateOrCreateFilter(request);
    }

    @Test
    public void ValidFilterCreationTest()
    {
        FilterCreateRequest request = new FilterCreateRequest(
            VALID_FILTER_NAME,
            validPermissions,
            validRoles
        );

        Response response = controller.updateOrCreateFilter(request);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getFilterResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void AddInvalidRoleTest()
    {
        HashSet<RolesOrPermissionDTO> addRoles = new HashSet<>();
        addRoles.add(new RolesOrPermissionDTO(INVALID_ROLE_ID,ROLE_NAME));
        FilterDTO filterDTO = new FilterDTO(FILTERID);
        filterDTO.setRoles(addRoles);
        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.ROLE_ID_INVALID, INVALID_ROLE_ID));
        controller.modifyFilter(filterDTO);
    }

    @Test
    public void AddInvalidPermissionTest()
    {
        HashSet<RolesOrPermissionDTO> addPermissions = new HashSet<>();
        addPermissions.add(new RolesOrPermissionDTO(INVALID_ROLE_ID,PERMISSION_NAME));
        FilterDTO filterDTO = new FilterDTO(FILTERID);
        filterDTO.setPermissions(addPermissions);

        exception.expect(WebApplicationException.class);
        exception.expectMessage(String.format(GroupErrorMessages.PERMISSION_ID_INVALID, INVALID_ROLE_ID));
        controller.modifyFilter(filterDTO);
    }

    @Test
    public void AddValidRoleTest()
    {
        HashSet<RolesOrPermissionDTO> addRoles = new HashSet<>();
        addRoles.add(new RolesOrPermissionDTO(8L,ROLE_NAME));
        FilterDTO filterDTO = new FilterDTO(FILTERID);
        filterDTO.setRoles(addRoles);
        filterDTO.setPermissions(new HashSet<>());

        Response response = controller.modifyFilter(filterDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getFilterResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void AddValidPermissionTest()
    {
        HashSet<RolesOrPermissionDTO> addPermissions = new HashSet<>();
        addPermissions.add(new RolesOrPermissionDTO(6L,PERMISSION_NAME));
        FilterDTO filterDTO = new FilterDTO(FILTERID);
        filterDTO.setPermissions(addPermissions);
        filterDTO.setRoles(new HashSet<>());

        Response response = controller.modifyFilter(filterDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getFilterResponse()).build();
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

        FilterDTO filterDTO = new FilterDTO(FILTERID);
        filterDTO.setPermissions(addPermissions);
        filterDTO.setRoles(addRoles);

        Response response = controller.modifyFilter(filterDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getFilterResponse()).build();
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

        FilterDTO groupDTO = new FilterDTO(FILTERID);
        groupDTO.setPermissions(removePermissions);
        groupDTO.setRoles(removeRoles);

        Response response = controller.modifyFilter(groupDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getFilterResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    @Test
    public void ModifyGroupWithEmptyRequestTest()
    {
        FilterDTO groupDTO = new FilterDTO(FILTERID);

        Response response =  controller.modifyFilter(groupDTO);
        Response validResponse = Response.status(Response.Status.CREATED).entity(getFilterResponse()).build();
        assertEquals(response.getStatus(), validResponse.getStatus());
        assertEquals(response.getEntity(),  validResponse.getEntity());
    }

    private FilterDTO getFilterResponse () {
        FilterDTO filterDTO = new FilterDTO(FILTERID);
        filterDTO.setName(VALID_FILTER_NAME);
        return filterDTO;
    }
}
