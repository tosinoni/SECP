package com.visucius.secp.auth;

import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.Role;
import com.visucius.secp.models.User;
import io.dropwizard.auth.Authorizer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SECPAuthorizer implements Authorizer<User> {
    private final static Logger LOG = LoggerFactory.getLogger(SECPAuthorizer.class);

    @Override
    public boolean authorize(User user, String role) {
        if (user == null) {
            LOG.error("Unable to authorize since user is null.");
            return false;
        } else if (user.getLoginRole() == null) {
            LOG.error("Unable to authorize since user's login role is null.");
            return false;
        } else if (StringUtils.isBlank(role)) {
            LOG.error("Unable to authorize since login role is blank.");
            return false;
        }

        LoginRole roleToEnum = null;
        try {
            roleToEnum = LoginRole.valueOf(role);
        } catch (IllegalArgumentException iae) {
            LOG.error("Unable to authorize since login role " + role + " is not a valid role.");
            return false;
        }

        return user.getLoginRole() == roleToEnum;
    }
}
