package com.visucius.secp.Controllers.User;

import com.google.common.base.Optional;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserController {
    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
    private UserDAO userDAO;


    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean isUserAnAdmin(String userID) {
        if(StringUtils.isBlank(userID) || !StringUtils.isNumeric(userID)) {
            LOG.warn("Empty user id provided.");
            return false;
        }

        long id = Long.parseLong(userID);
        Optional<User> user = userDAO.find(id);

        if (!user.isPresent()) {
            LOG.warn("User not found.");
            return false;
        }

        return user.get().getLoginRole().equals(LoginRole.ADMIN);
    }
}
