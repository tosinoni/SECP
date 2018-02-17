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
    public Response registerAdmin(@PathParam("id") String id) {
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
    public Response create(UserRegistrationRequest request) {
        return userRegistrationController.registerUser(request);
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
    public Response createPermissions(AppCreateDTO request) {
        return adminController.registerPermissions(request);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/roles/id/{id}")
    public Response updateRole(@Auth RolesOrPermissionDTO request, @PathParam("id") String id) {
        return adminController.updateRoles(request,id);
    }

    @POST
    @Timed
    @UnitOfWork
    @Path("/permissions/id/{id}")
    public Response updatePermissions(@Auth RolesOrPermissionDTO request, @PathParam("id") String id) {
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

    @POST
    @Path("/audit/user")
    @UnitOfWork
    public Response getUserAudit(@Auth User user, AuditDTO userAuditDTO) {
        return adminController.getUserAudit(user, userAuditDTO);
    }

    @POST
    @Path("/audit/groups")
    @UnitOfWork
    public Response getUserAudit(@Auth AuditDTO groupAuditDTO) {
        return adminController.getGroupsAudit(groupAuditDTO);
    }

    @GET
    @Path("/ledger")
    @UnitOfWork
    public Response getLedger() {
        return adminController.getLedger();
    }
}
