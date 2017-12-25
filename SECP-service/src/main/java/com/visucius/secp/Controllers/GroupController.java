package com.visucius.secp.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.visucius.secp.DTO.GroupCreationRequest;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Role;
import com.visucius.secp.models.User;
import com.visucius.secp.util.InputValidator;
import com.visucius.secp.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupController {

    private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);
    public static final int MINIMUM_AMOUNT_OF_USERS = 2;
    public static final int MAXIMUM_AMOUNT_OF_USERS = 50;

    private GroupDAO groupRepository;
    private UserDAO userRepository;
    private RolesDAO rolesRepository;

    public GroupController(GroupDAO groupRepository, UserDAO userRepository, RolesDAO rolesRepository)
    {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }


    public Response createGroup(GroupCreationRequest request)
    {
        List<String> errors = validateCreationRequest(request);

        if(!errors.isEmpty())
        {
            try
            {
                throw new WebApplicationException(
                    JsonUtil.convertToJsonString(errors),
                    Response.Status.BAD_REQUEST);
            }
            catch (JsonProcessingException exception)
            {
                LOG.error(exception.getMessage());
                throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
            }
        }

        Group group = new Group(request.name);
        group.setRoles(getRoles(request.roles));
        group.setUsers(getUsers(request.users));
        Group createdGroup = groupRepository.save(group);

        return Response.status(Response.Status.CREATED).entity(createdGroup.getId()).build();
    }


    private List<String> validateCreationRequest(GroupCreationRequest request)
    {
        List<String> errors = new ArrayList<>();

        if(!isGroupNameValid(request.name)
            || !InputValidator.isNameValid(request.name))
        {
            errors.add(GroupErrorMessages.GROUP_NAME_INVALID);
        }


        for (long roleID: request.roles) {
            if(!isRoleIdValid(roleID))
            {
                errors.add(String.format(GroupErrorMessages.ROLE_ID_INVALID,roleID));
            }
        }


        if(request.users.size() < MINIMUM_AMOUNT_OF_USERS)
        {
            errors.add(GroupErrorMessages.GROUP_TOO_SMALL);
        }
        else if (request.users.size() > MAXIMUM_AMOUNT_OF_USERS)
        {
            errors.add(GroupErrorMessages.GROUP_TOO_BIG);
        }
        else {
            for (long userId : request.users) {
                if (!isUserIDValid(userId)) {
                    errors.add(String.format(GroupErrorMessages.USER_ID_INVALID, userId));
                }
            }
        }

        return errors;
    }

    private Set<User> getUsers(Set<Long> userIDs)
    {
        Set<User> users = new HashSet<>();
        userIDs.forEach(id -> users.add(userRepository.find(id).get()));
        return users;
    }

    private Set<Role> getRoles(Set<Long> rolesIDs)
    {
        Set<Role> roles = new HashSet<>();
        rolesIDs.forEach(id -> roles.add(rolesRepository.find(id).get()));
        return roles;
    }


    private boolean isUserIDValid(long id)
    {
        return userRepository.find(id).isPresent();
    }

    private boolean isRoleIdValid(long id)
    {
        return rolesRepository.find((int)id).isPresent();
    }

    private boolean isGroupNameValid(String name)
    {
        return groupRepository.findByName(name) == null;
    }
}
