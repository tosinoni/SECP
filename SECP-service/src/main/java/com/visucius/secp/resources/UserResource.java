package com.visucius.secp.resources;

import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.User;
import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import javax.ws.rs.NotFoundException;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.BooleanParam;
import io.dropwizard.jersey.params.DateTimeParam;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import org.hibernate.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
public class UserResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    private final UserDAO dao;

    public UserResource(UserDAO dao) {
        this.dao = dao;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed 
    @UnitOfWork
    public User create(User entity) {
        return dao.save(entity);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public List<User> getAll() {
        return dao.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Timed
    @UnitOfWork
    public User get(@PathParam("id") LongParam id) {
        Optional<User> entity = dao.find(id.get());
        if (!entity.isPresent()) {
            throw new NotFoundException("User " + id.get() + " not found");
        }
        return entity.get();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timed 
    @UnitOfWork
    public User update(@PathParam("id") LongParam id, User entity) {
        Optional<User> ent = dao.find(id.get());
        if (!ent.isPresent()) {
            throw new NotFoundException("User " + id.get() + " not found");
        }
        return dao.merge(entity);
    }

    @DELETE
    @Path("{id}")
    @Timed
    @UnitOfWork
    public void delete(@PathParam("id") LongParam id) {
        Optional<User> entity = dao.find(id.get());
        if (!entity.isPresent()) {
            throw new NotFoundException("User " + id.get() + " not found");
        }
        dao.delete(entity.get());
    }
}
