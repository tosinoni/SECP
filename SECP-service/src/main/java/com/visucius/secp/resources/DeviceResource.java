package com.visucius.secp.resources;

import com.visucius.secp.Controllers.User.DeviceController;
import com.visucius.secp.DTO.DeviceDTO;
import com.visucius.secp.DTO.SecretDTO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public Response addSecretGroupForDevices(@Auth Set<SecretDTO> secretDTOS) {
        return deviceController.addSecretGroupForDevices(secretDTOS);
    }
}
