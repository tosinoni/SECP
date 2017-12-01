package com.visucius.secp.Controllers.User;

import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.SECPService;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.User;
import com.visucius.secp.util.InputValidator;
import com.visucius.secp.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class UserRegistrationController{

    private static final Logger LOG = LoggerFactory.getLogger(SECPService.class);

    private final UserDAO userDAO;



    public UserRegistrationController(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {

        List<String> errors = validateInput(request);
        if(isUsernameValid(request.userName)) {
            errors.add(UserErrorMessage.DUPLICATE_USERNAME);
        }

        if(isEmailValid(request.email))
        {
            errors.add(UserErrorMessage.DUPLICATE_EMAIL);
        }

        if(errors.isEmpty())
        {
            try {
                String hashPassword = PasswordUtil.createHash(request.password);
                User user = new User(request.firstName, request.lastName, request.userName, request.email, hashPassword);
                User createdUser = userDAO.save(user);
                return new UserRegistrationResponse(true, UserErrorMessage.USER_CREATED, Response.Status.CREATED, errors, createdUser.getId());

            } catch (PasswordUtil.CannotPerformOperationException e) {
                LOG.error(e.getLocalizedMessage());
            }
            catch (Exception e)
            {
                LOG.error(e.getLocalizedMessage());
            }
        }

        return new UserRegistrationResponse(false, UserErrorMessage.USER_NOT_CREATED, Response.Status.BAD_REQUEST,errors);
    }

    private List<String> validateInput(UserRegistrationRequest request)
    {
        List<String> errors = new ArrayList<>();

        if(!InputValidator.isNameValid(request.firstName))
        {
            errors.add(UserErrorMessage.FIRST_NAME_INVALID);
        }

        if(!InputValidator.isNameValid(request.lastName))
        {
            errors.add(UserErrorMessage.LAST_NAME_INVALID);

        }

        if(!InputValidator.isNameValid(request.userName))
        {
            errors.add(UserErrorMessage.User_NAME_INVALID);
        }

        if(!InputValidator.isEmailValid(request.email))
        {
            errors.add(UserErrorMessage.EMAIL_INVALID);

        }

        if(!InputValidator.isPasswordValid(request.password))
        {
            errors.add(UserErrorMessage.PASSWORD_INVALID);
        }

        return errors;
    }

    public boolean isUsernameValid(String userName)
    {
        return findUserByUsername(userName) != null;
    }

    private User findUserByUsername(String userName)
    {
        return userDAO.findByUserName(userName);
    }


    public boolean isEmailValid(String email)
    {
        return userDAO.findByEmail(email) != null;
    }
}

