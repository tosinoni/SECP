package com.visucius.secp.UseCase;

import com.visucius.secp.DTO.LoginRequestDTO;
import com.visucius.secp.DTO.TokenDTO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.User;
import com.visucius.secp.util.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class LoginRequestController {

    private final static Logger LOG = LoggerFactory.getLogger(LoginRequestController.class);
    private UserDAO userDAO;
    private TokenController tokenController;

    //error messages
    private static final String LOGIN_FAIL_NO_CREDENTIALS = "Login Failed. Please provide your username and password.";
    private static final String LOGIN_FAIL_NO_USERNAME = "Login Failed. Please provide your username.";
    private static final String LOGIN_FAIL_NO_PASSWORD = "Login Failed. Please provide your password.";
    private static final String LOGIN_FAIL_USER_NOT_FOUND = "Login Failed. username does not exist.";
    private static final String LOGIN_FAIL_WRONG_PASSWORD = "Login Failed. Incorrect password.";


    public LoginRequestController(TokenController tokenController, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.tokenController = tokenController;
    }

    public Response login(LoginRequestDTO loginRequestDTO) {
        validate(loginRequestDTO);

        User user = userDAO.findByUserName(loginRequestDTO.getUsername());

        if (user == null) {
            LOG.warn("User not found.");
            throw new WebApplicationException(LOGIN_FAIL_USER_NOT_FOUND, Response.Status.UNAUTHORIZED);
        }

        boolean isValidPassword = isPasswordValid(loginRequestDTO, user);

        if (isValidPassword) {
            String token = getToken(loginRequestDTO);

            TokenDTO tokenDTO = new TokenDTO(token, user.getLoginRole());
            return Response.ok().entity(tokenDTO).build();
        } else {
            throw new WebApplicationException(LOGIN_FAIL_WRONG_PASSWORD, Response.Status.UNAUTHORIZED);
        }
    }

    private boolean isPasswordValid(LoginRequestDTO loginRequestDTO, User user) {
        boolean isPasswordValid;

        try {
            isPasswordValid = PasswordUtil.verifyPassword(loginRequestDTO.getPassword(), user.getPassword());
        } catch (PasswordUtil.CannotPerformOperationException e) {
            LOG.error("Unable to compute hash.", e);
            throw new WebApplicationException(LOGIN_FAIL_WRONG_PASSWORD, Response.Status.UNAUTHORIZED);
        } catch (PasswordUtil.InvalidHashException e) {
            LOG.warn("Unable to compute hash. ", e);
            throw new WebApplicationException(LOGIN_FAIL_WRONG_PASSWORD, Response.Status.UNAUTHORIZED);
        }

        return isPasswordValid;
    }

    private String getToken(LoginRequestDTO loginRequestDTO) {
        String token;

        try {
            token = tokenController.createTokenFromUsername(loginRequestDTO.getUsername());
        } catch (JoseException e) {
            LOG.error("Unable to create authToken.", e);
            throw new WebApplicationException(LOGIN_FAIL_USER_NOT_FOUND, Response.Status.UNAUTHORIZED);
        }

        return token;
    }

    private void validate(LoginRequestDTO loginRequestDTO) {
        if (loginRequestDTO == null) {
            throw new WebApplicationException(LOGIN_FAIL_NO_CREDENTIALS, Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(loginRequestDTO.getUsername())) {
            throw new WebApplicationException(LOGIN_FAIL_NO_USERNAME, Response.Status.BAD_REQUEST);
        } else if (StringUtils.isBlank(loginRequestDTO.getPassword())) {
            throw new WebApplicationException(LOGIN_FAIL_NO_PASSWORD, Response.Status.BAD_REQUEST);
        }
    }
}
