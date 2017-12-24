package com.visucius.secp.resources;

import com.visucius.secp.Controllers.Admin.AdminController;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RolesAllowed("NORMAL")
@Path("/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {
    private final AdminController adminController;

    public AdminResource(AdminController adminController) {
        this.adminController = adminController;
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
}
