package com.visucius.secp.Controllers.User;

import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Response;

public class UserController {
    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
    private UserDAO userDAO;


    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean isUserAnAdmin(String userID) {
        long id = Long.parseLong(userID);

        User user = userDAO.find(id).get();

        if (user == null) {
            LOG.warn("User not found.");
            return false;
        }
        return user.getLoginRole().equals(LoginRole.ADMIN);
    }
}
