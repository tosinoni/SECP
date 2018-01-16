package com.visucius.secp.resources;

import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.Controllers.Admin.AdminController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.DTO.AppCreateDTO;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RolesAllowed("ADMIN")
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {
    private final AdminController adminController;
    private final UserRegistrationController userRegistrationController;


    public AdminResource(AdminController adminController, UserRegistrationController userRegistrationController) {
        this.adminController = adminController;
        this.userRegistrationController = userRegistrationController;
    }

    @POST
    @Path("{id}")
    @UnitOfWork
    public Response registerAdmin(@Auth @PathParam("id") String id) {
        adminController.registerAdmin(id);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{id}")
    @UnitOfWork
    public Response removeAdmin(@Auth @PathParam("id") String id) {
        adminController.removeAdmin(id);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    @Path("/register")
    public Response create(@Auth UserRegistrationRequest request) {

        UserRegistrationResponse response = userRegistrationController.registerUser(request);

        if (response.success) {
            return Response.status(response.status).entity(response.toString()).build();
        }

        throw new WebApplicationException(response.getErrors(), response.status);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/roles")
    public Response createRoles(@Auth AppCreateDTO request) {
        return adminController.registerRoles(request);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/permissions")
    public Response createPermissions(@Auth AppCreateDTO request) {
        return adminController.registerPermissions(request);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/roles/id{id}")
    public Response updateRole(@Auth AppCreateDTO request, @PathParam("id") String id) {
        return adminController.updateRoles(request,id);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/permissions/id/{id}")
    public Response createPermissions(@Auth AppCreateDTO request, @PathParam("id") String id) {
        return adminController.updatePermissions(request,id);
    }

    @DELETE
    @Path("/role/id/{id}")
    @UnitOfWork
    public Response deleteRole(@Auth @PathParam("id") String id) {
        return adminController.deleteRole(id);
    }

    @DELETE
    @Path("/permission/id/{id}")
    @UnitOfWork
    public Response deletePermission(@Auth @PathParam("id") String id) {
        return adminController.deletePermission(id);
    }

    @GET
    @Path("/roles")
    @UnitOfWork
    public Response getAllRoles() {
        return adminController.getAllRoles();
    }

    @GET
    @Path("/permissions")
    @UnitOfWork
    public Response getAllPermissions() {
        return adminController.getAllPermissions();
    }
}
