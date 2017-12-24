package com.visucius.secp.Controllers.Admin;

import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AdminController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private final UserDAO userDAO;

    public AdminController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void registerAdmin(String userID) {
        long id = Long.parseLong(userID);

        User user = userDAO.find(id).get();

        if (user == null) {
            LOG.warn("User not found.");
            throw new WebApplicationException(AdminErrorMessage.REGISTER_ADMIN_FAIL_INVALID_USER, Response.Status.BAD_REQUEST);
        }
        else if(user.getLoginRole().equals(LoginRole.ADMIN)) {
            LOG.warn("Admin registration failed. User has already been registered as admin.");
            throw new WebApplicationException(AdminErrorMessage.REGISTER_ADMIN_FAIL_DUPLICATE_ENTRY, Response.Status.BAD_REQUEST);
        }

        user.setLoginRole(LoginRole.ADMIN);
        userDAO.save(user);
        LOG.info("New admin created.");
    }

    public void removeAdmin(String userID) {
        long id = Long.parseLong(userID);

        User user = userDAO.find(id).get();

        if (user == null || !user.getLoginRole().equals(LoginRole.ADMIN)) {
            LOG.warn("Delete admin failed. User is not an admin.");
            throw new WebApplicationException(AdminErrorMessage.DELETE_ADMIN_FAIL_INVALID_USER, Response.Status.BAD_REQUEST);
        }

        user.setLoginRole(LoginRole.NORMAL);
        userDAO.save(user);
        LOG.info("An admin has been removed.");
    }
}
