package com.visucius.secp.resources;

import com.visucius.secp.Controllers.User.UserController;
import com.visucius.secp.DTO.LoginRequestDTO;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.Controllers.User.LoginRequestController;
import com.visucius.secp.Controllers.User.UserRegistrationController;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class EntryResource {

    private static final Logger LOG = LoggerFactory.getLogger(EntryResource.class);

    private final LoginRequestController loginRequestController;

    public EntryResource(LoginRequestController loginRequestController) {

        this.loginRequestController = loginRequestController;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    @Path("/login")
    public Response login(LoginRequestDTO loginRequestDTO) {
        return loginRequestController.login(loginRequestDTO);
    }
}
