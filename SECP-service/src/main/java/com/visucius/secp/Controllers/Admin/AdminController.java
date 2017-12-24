package com.visucius.secp.Controllers.Admin;

import com.google.common.base.Optional;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import org.apache.commons.lang3.StringUtils;
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
        validateInput(userID);
        User user = getUserFromID(userID);

        if(user.getLoginRole().equals(LoginRole.ADMIN)) {
            LOG.warn("Admin registration failed. User has already been registered as admin.");
            throw new WebApplicationException(AdminErrorMessage.REGISTER_ADMIN_FAIL_DUPLICATE_ENTRY, Response.Status.BAD_REQUEST);
        }

        user.setLoginRole(LoginRole.ADMIN);
        userDAO.save(user);
        LOG.info("New admin created.");
    }

    public void removeAdmin(String userID) {
        validateInput(userID);
        User user = getUserFromID(userID);

        if (!user.getLoginRole().equals(LoginRole.ADMIN)) {
            LOG.warn("Delete admin failed. User is not an admin.");
            throw new WebApplicationException(AdminErrorMessage.DELETE_ADMIN_FAIL_INVALID_USER, Response.Status.NO_CONTENT);
        }

        user.setLoginRole(LoginRole.NORMAL);
        userDAO.save(user);
        LOG.info("An admin has been removed.");
    }

    private void validateInput(String userID) {
        if(StringUtils.isBlank(userID) || !StringUtils.isNumeric(userID)) {
            LOG.warn("Empty user id provided.");
            throw new WebApplicationException(AdminErrorMessage.INVALID_USER_ID, Response.Status.BAD_REQUEST);
        }
    }

    private User getUserFromID(String userID) {
        long id = Long.parseLong(userID);

        Optional<User> user = userDAO.find(id);

        if (!user.isPresent()) {
            LOG.warn("User not found.");
            throw new WebApplicationException(AdminErrorMessage.USER_DOES_NOT_EXIST, Response.Status.BAD_REQUEST);
        }

        return user.get();
    }
}
