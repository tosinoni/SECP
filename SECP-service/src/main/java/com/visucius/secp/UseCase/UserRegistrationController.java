package com.visucius.secp.UseCase;

import com.visucius.secp.Contracts.IRequestHandler;
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

public class UserRegistrationController implements IRequestHandler<UserRegistrationRequest, UserRegistrationResponse> {

    private static final Logger LOG = LoggerFactory.getLogger(SECPService.class);

    private final UserDAO userDAO;

    public static final String FIRST_NAME_INVALID = "First name is not valid.";
    public static final String LAST_NAME_INVALID = "Last name is not valid.";
    public static final String User_NAME_INVALID = "Username is not valid.";
    public static final String EMAIL_INVALID = "Email is not valid.";
    public static final String PASSWORD_INVALID = "Password is not valid";
    public static final String USER_CREATED = "User Created";
    public static final String USER_NOT_CREATED = "User not created";
    public static final String DUPLICATE_USERNAME = "Username already exists";
    public static final String DUPLICATE_EMAIL = "Email is in use";

    public UserRegistrationController(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    @Override
    public UserRegistrationResponse handle(UserRegistrationRequest request) {

        List<String> errors = validateInput(request);
        if(isUserNameInUse(request.userName)) {
            errors.add(DUPLICATE_USERNAME);
        }

        if(isEmailInUser(request.email))
        {
            errors.add(DUPLICATE_EMAIL);
        }

        if(errors.isEmpty())
        {
            try {
                String hashPassword = PasswordUtil.createHash(request.password);
                User user = new User(request.firstName, request.lastName, request.userName, request.email, hashPassword);
                User createdUser = userDAO.save(user);
                return new UserRegistrationResponse(true, USER_CREATED, Response.Status.CREATED, errors, createdUser.getId());

            } catch (PasswordUtil.CannotPerformOperationException e) {
                LOG.error(e.getLocalizedMessage());
            }
            catch (Exception e)
            {
                LOG.error(e.getLocalizedMessage());
            }
        }

        return new UserRegistrationResponse(false,USER_NOT_CREATED, Response.Status.BAD_REQUEST,errors);
    }

    private List<String> validateInput(UserRegistrationRequest request)
    {
        List<String> errors = new ArrayList<>();

        if(!InputValidator.isNameValid(request.firstName))
        {
            errors.add(FIRST_NAME_INVALID);
        }

        if(!InputValidator.isNameValid(request.lastName))
        {
            errors.add(LAST_NAME_INVALID);

        }

        if(!InputValidator.isNameValid(request.userName))
        {
            errors.add(User_NAME_INVALID);
        }

        if(!InputValidator.isEmailValid(request.email))
        {
            errors.add(EMAIL_INVALID);

        }

        if(!InputValidator.isPasswordValid(request.password))
        {
            errors.add(PASSWORD_INVALID);
        }

        return errors;
    }

    private boolean isUserNameInUse(String userName)
    {
        return userDAO.findByUserName(userName) != null;
    }

    private boolean isEmailInUser(String email)
    {
        return userDAO.findByEmail(email) != null;
    }
}

