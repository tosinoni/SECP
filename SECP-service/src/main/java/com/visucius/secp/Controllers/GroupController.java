package com.visucius.secp.Controllers;

import com.visucius.secp.DTO.GroupCreationRequest;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.PermissionLevelDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Role;
import com.visucius.secp.models.User;
import com.visucius.secp.util.InputValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

public class GroupController {

    private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);
    public static final int MINIMUM_AMOUNT_OF_ROLES = 1;

    private GroupDAO groupRepository;
    private UserDAO userRepository;
    private RolesDAO rolesRepository;
    private PermissionLevelDAO permissionsRepository;

    public GroupController(GroupDAO groupRepository,
                           UserDAO userRepository,
                           RolesDAO rolesRepository,
                           PermissionLevelDAO permissionLevelDAO)
    {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.permissionsRepository = permissionLevelDAO;
    }


    public Response createGroup(GroupCreationRequest request)
    {

        String error = validateCreationRequest(request);
        if(StringUtils.isNoneEmpty(error))
        {
                throw new WebApplicationException(
                    error,
                    Response.Status.BAD_REQUEST);
        }

        Group group = new Group(request.name);
        group.setRoles(getRoles(request.roles));
        group.setUsers(getUsers(request.roles));
        Group createdGroup = groupRepository.save(group);

        return Response.status(Response.Status.CREATED).entity(createdGroup.getId()).build();
    }


    private String validateCreationRequest(GroupCreationRequest request) {

        if (!isGroupNameValid(request.name)
            || !InputValidator.isNameValid(request.name)) {
            return GroupErrorMessages.GROUP_NAME_INVALID;
        }

        for (long userId : request.permissionLevels) {
            if (!isPermissionValid(userId)) {
                return String.format(GroupErrorMessages.Permission_ID_INVALID, userId);
            }
        }

        if(request.roles.size() < MINIMUM_AMOUNT_OF_ROLES)
        {
            return GroupErrorMessages.GROUP_ROLES_REQUIRED;
        }

        for (long roleID : request.roles) {
            if (!isRoleIdValid(roleID)) {
                return String.format(GroupErrorMessages.ROLE_ID_INVALID, roleID);
            }
        }

        return StringUtils.EMPTY;
    }

    private Set<User> getUsers(Set<Long> rolesIDs)
    {
        Set<User> users = new HashSet<>();
        rolesIDs.forEach(id -> users.addAll(userRepository.findUsersWithRole(id)));
        return users;
    }

    private Set<Role> getRoles(Set<Long> rolesIDs)
    {
        Set<Role> roles = new HashSet<>();
        rolesIDs.forEach(id -> roles.add(rolesRepository.find(id).get()));
        return roles;
    }


    private boolean isPermissionValid(long id)
    {
        return permissionsRepository.find(id).isPresent();
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
