package com.visucius.secp.resources;

import com.google.common.base.Optional;
import com.visucius.secp.Controllers.FilterController;
import com.visucius.secp.daos.FilterDAO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RecordsDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.helpers.ResponseValidator;
import com.visucius.secp.models.Filter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;
import java.util.Arrays;

public class FilterResourceTest {

    private static final String filterUrl = "/filter";
    private static final String getFilterUrl = "/filter/id/";

    private FilterDAO filterDAO = Mockito.mock(FilterDAO.class);
    private RolesDAO rolesDAO = Mockito.mock(RolesDAO.class);
    private PermissionDAO permissionDAO = Mockito.mock(PermissionDAO.class);
    private RecordsDAO recordsDAO = Mockito.mock(RecordsDAO.class);


    private FilterController filterController = new FilterController(filterDAO, rolesDAO, permissionDAO, recordsDAO);

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
        .addResource(new FilterResource(filterController))
        .build();

    @Test
    public void testGetAllFilters() {
        //testing empty filter words in db
        Response response = resources.client().target(filterUrl).request().get();
        ResponseValidator.validate(response, 204);

        //testing with roles in db
        Filter filter = new Filter("developer");
        Mockito.when(filterDAO.findAll()).thenReturn(Arrays.asList(filter));
        response = resources.client().target(filterUrl).request().get();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testDeleteFilter() {

        long id = 12;
        Filter mockedFilter = new Filter("developer");

        Mockito.when(filterDAO.find(id)).thenReturn(Optional.fromNullable(mockedFilter));
        Response response = resources.client().target(filterUrl + "/" + 12).request().delete();
        ResponseValidator.validate(response, 200);
    }

    @Test
    public void testGetFilterWithInvalidId() {
        //testing get filter with no id
        Response response = resources.client().target(getFilterUrl + null).request().get();
        ResponseValidator.validate(response, 204);

        //testing get filter with empty id
        response = resources.client().target(getFilterUrl + " ").request().get();
        ResponseValidator.validate(response, 204);

        //testing get filter with alphanumeric as id
        response = resources.client().target(getFilterUrl + "abc").request().get();
        ResponseValidator.validate(response, 204);

        //testing get filter with invalid id
        long id = 1;
        Optional<Filter> filter = Optional.absent();
        Mockito.when(filterDAO.find(id)).thenReturn(filter);
        response = resources.client().target(getFilterUrl + id).request().get();
        ResponseValidator.validate(response, 400);
    }

    @Test
    public void testGetFilterWithValidFilterId() {
        long id = 12;
        Filter mockedFilter = new Filter("developer");

        Optional<Filter> filter = Optional.of(mockedFilter);
        Mockito.when(filterDAO.find(id)).thenReturn(filter);

        Response response = resources.client().target(getFilterUrl + id).request().get();
        ResponseValidator.validate(response, 200);
    }
}
