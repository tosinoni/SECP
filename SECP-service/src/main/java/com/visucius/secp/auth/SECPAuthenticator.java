package com.visucius.secp.auth;

import com.visucius.secp.Controllers.TokenController;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.hibernate.UnitOfWork;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class SECPAuthenticator implements Authenticator<String, User> {

    private final static Logger LOG = LoggerFactory.getLogger(SECPAuthenticator.class);
    private UserDAO userDAO;
    private TokenController tokenController;

    public SECPAuthenticator(UserDAO userDAO, TokenController tokenController) {
        this.userDAO = userDAO;
        this.tokenController = tokenController;
    }

    @Override
    @UnitOfWork
    public Optional<User> authenticate(String token) throws AuthenticationException {
        String username;

        try {
            username = tokenController.getUsernameFromToken(token);
        } catch (InvalidJwtException e) {
            throw new AuthenticationException(e);
        }

        if (StringUtils.isBlank(username)) {
            LOG.error("Username is blank.");
            return Optional.empty();
        } else {
            User user = userDAO.findByUserName(username);
            return Optional.ofNullable(user);
        }
    }
}
