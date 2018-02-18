package com.visucius.secp.resources;

import com.codahale.metrics.annotation.Timed;
import com.visucius.secp.Controllers.Admin.AdminController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.visucius.secp.DTO.*;
import com.visucius.secp.models.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
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
    public Response registerAdmin(@Auth User user, @PathParam("id") String id) {
        adminController.registerAdmin(user, id);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{id}")
    @UnitOfWork
    public Response removeAdmin(@Auth User user, @PathParam("id") String id) {
        adminController.removeAdmin(user, id);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    @Path("/register")
    public Response create(@Auth User user, UserRegistrationRequest request) {
        return userRegistrationController.registerUser(user, request);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/roles")
    public Response createRoles(@Auth User user, AppCreateDTO request) {
        return adminController.registerRoles(user, request);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/permissions")
    public Response createPermissions(@Auth User user, AppCreateDTO request) {
        return adminController.registerPermissions(user, request);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/roles/id/{id}")
    public Response updateRole(@Auth User user, RolesOrPermissionDTO request, @PathParam("id") String id) {
        return adminController.updateRoles(user, request,id);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/permissions/id/{id}")
    public Response updatePermissions(@Auth User user, RolesOrPermissionDTO request, @PathParam("id") String id) {
        return adminController.updatePermissions(user, request,id);
    }

    @DELETE
    @Path("/role/id/{id}")
    @UnitOfWork
    public Response deleteRole(@Auth User user, @PathParam("id") String id) {
        return adminController.deleteRole(user, id);
    }

    @DELETE
    @Path("/permission/id/{id}")
    @UnitOfWork
    public Response deletePermission(@Auth User user, @PathParam("id") String id) {
        return adminController.deletePermission(user, id);
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

    @POST
    @Path("/audit/user")
    @UnitOfWork
    public Response getUserAudit(@Auth User user, AuditDTO userAuditDTO) {
        return adminController.getUserAudit(user, userAuditDTO);
    }

    @POST
    @Path("/audit/groups")
    @UnitOfWork
    public Response getGroupAudit(@Auth User user, AuditDTO groupAuditDTO) {
        return adminController.getGroupsAudit(user, groupAuditDTO);
    }

    @GET
    @Path("/ledger")
    @UnitOfWork
    public Response getLedger() {
        return adminController.getLedger();
    }
}
