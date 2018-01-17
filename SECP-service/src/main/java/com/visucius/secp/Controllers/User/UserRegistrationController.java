package com.visucius.secp.Controllers.User;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.User;
import com.visucius.secp.util.InputValidator;
import com.visucius.secp.util.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class UserRegistrationController{

    private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationController.class);

    private final UserDAO userDAO;
    private final PermissionDAO permissionDAO;

    public UserRegistrationController(UserDAO userDAO, PermissionDAO permissionDAO)
    {

        this.userDAO = userDAO;
        this.permissionDAO = permissionDAO;
    }

    public Response registerUser(UserRegistrationRequest request) {

        String error= validateInput(request);
        if(isUsernameValid(request.userName)) {
            throw new WebApplicationException(
                UserErrorMessage.DUPLICATE_USERNAME,
                Response.Status.BAD_REQUEST);
        }

        if(isEmailValid(request.email))
        {
            throw new WebApplicationException(
                UserErrorMessage.DUPLICATE_EMAIL,
                Response.Status.BAD_REQUEST);
        }

        if(error.isEmpty())
        {
            try {
                String hashPassword = PasswordUtil.createHash(request.password);
                User user = new User(request.firstName, request.lastName, request.userName, request.email, hashPassword);
                Permission permission = getPermission(request.permission.getId());
                user.setPermission(permission);
                User createdUser = userDAO.save(user);
                UserDTO createdUserDTO = new UserDTO(createdUser);
                return Response.status(Response.Status.CREATED).entity(createdUserDTO).build();

            } catch (PasswordUtil.CannotPerformOperationException e) {
                LOG.error(e.getLocalizedMessage());
            }
            catch (Exception e)
            {
                LOG.error(e.getLocalizedMessage());
            }
        }

        throw new WebApplicationException(
            error,
            Response.Status.BAD_REQUEST);
    }

    private String validateInput(UserRegistrationRequest request)
    {
        if(!InputValidator.isNameValid(request.firstName))
        {
            return  UserErrorMessage.FIRST_NAME_INVALID;
        }

        if(!InputValidator.isNameValid(request.lastName))
        {
            return UserErrorMessage.LAST_NAME_INVALID;

        }
        if(!InputValidator.isNameValid(request.userName))
        {
             return UserErrorMessage.User_NAME_INVALID;
        }
        if(!InputValidator.isEmailValid(request.email))
        {
            return UserErrorMessage.EMAIL_INVALID;

        }

        if(!InputValidator.isPasswordValid(request.password))
        {
            return UserErrorMessage.PASSWORD_INVALID;
        }

        return "";
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

    private Permission getPermission(long id)
    {
        Optional<Permission> permissionOptional = this.permissionDAO.find(id);
        if(!permissionOptional.isPresent())
        {
            throw new WebApplicationException(
                UserErrorMessage.MODIFY_USER_FAIL_PERMISSIONS_INVALID,
                Response.Status.BAD_REQUEST);
        }
        return permissionOptional.get();
    }

}

