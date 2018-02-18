package com.visucius.secp.Controllers.User;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.DTO.UserRegistrationRequest;
import com.visucius.secp.DTO.UserRegistrationResponse;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RecordsDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.*;
import com.visucius.secp.util.InputValidator;
import com.visucius.secp.util.PasswordUtil;
import com.visucius.secp.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRegistrationController{

    private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationController.class);

    private final UserDAO userDAO;
    private final GroupDAO groupDAO;
    private final PermissionDAO permissionDAO;
    private final RecordsDAO recordsDAO;


    public UserRegistrationController(UserDAO userDAO, PermissionDAO permissionDAO,
                                      GroupDAO groupDAO, RecordsDAO recordsDAO)
    {

        this.userDAO = userDAO;
        this.permissionDAO = permissionDAO;
        this.groupDAO = groupDAO;
        this.recordsDAO = recordsDAO;
    }

    public Response registerUser(User requestUser, UserRegistrationRequest request) {

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
                user.setAvatarUrl(UserDTO.defaultUserAvatar);
                user.setDisplayName(request.userName);
                Permission permission = getPermission(request.permission.getId());
                user.setPermission(permission);
                user.setGroups(getUserGroups(user));
                User createdUser = userDAO.save(user);
                UserDTO createdUserDTO = new UserDTO(createdUser);

                //adding the record to ledger
                String action = user.getFirstname() + " " + user.getLastname() + " was created";
                recordsDAO.save(Util.createRecord(requestUser, ActionType.USER, action));

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

    private boolean isUserInGroup(Group group, User user) {
        return group.getGroupType().equals(GroupType.PUBLIC)
            && (group.getPermissions().isEmpty() || group.getPermissions().contains(user.getPermission()))
            && (group.getRoles().isEmpty() || !Sets.intersection(group.getRoles(), user.getRoles()).isEmpty());
    }
    private Set<Group> getUserGroups(User user) {
        //get user's new group
        Set<Group> groupsForUser = groupDAO.findAll().stream()
            .filter(group -> isUserInGroup(group, user))
            .map(group -> {
                group.getUsers().add(user);
                groupDAO.save(group);
                return group;
            }).collect(Collectors.toSet());

        return groupsForUser;
    }

}

