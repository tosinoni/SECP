package com.visucius.secp.resources;

import com.visucius.secp.Controllers.User.DeviceController;
import com.visucius.secp.DTO.SecretDTO;
import com.visucius.secp.models.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/device")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"NORMAL", "ADMIN"})
public class DeviceResource {

    private final DeviceController deviceController;

    public DeviceResource(DeviceController deviceController) {
        this.deviceController = deviceController;
    }

    @POST
    @UnitOfWork
    @Path("/secret")
    public Response addSecretKeyForDevices(@Auth Set<SecretDTO> secretDTOS) {
        return deviceController.addSecretKeyForDevices(secretDTOS);
    }

    @GET
    @UnitOfWork
    @Path("/group/{id}")
    public Response getDevicesForGroup(@Auth @PathParam("id") long groupID) {
        return deviceController.getDevicesForGroup(groupID);
    }

    @GET
    @UnitOfWork
    @Path("/user/{id}")
    public Response getDevicesForUser(@Auth @PathParam("id") long userID) {
        return deviceController.getDevicesForUser(userID);
    }

    @GET
    @UnitOfWork
    @Path("/{name}/secret")
    public Response getSecretKeysForDevice(@Auth User user, @PathParam("name") String deviceName) {
        return deviceController.getDeviceSecretForUser(user, deviceName);
    }
}
