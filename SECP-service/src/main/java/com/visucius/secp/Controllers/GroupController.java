package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.visucius.secp.DTO.GroupRequest;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.PermissionLevelDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.PermissionLevel;
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
    public static final int MINIMUM_AMOUNT_OF_PERMISSIONS = 1;

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


    public Response createGroup(GroupRequest request)
    {

        String error = validateRequest(request);
        if(StringUtils.isNoneEmpty(error))
        {
                throw new WebApplicationException(
                    error,
                    Response.Status.BAD_REQUEST);
        }

        Group group = new Group(request.name);
        group.setRoles(getRoles(request.roles));
        group.setUsers(getUsers(request.permissionLevels,request.roles));
        group.setPermissionLevels(getPermissions(request.permissionLevels));
        Group createdGroup = groupRepository.save(group);

        return Response.status(Response.Status.CREATED).entity(createdGroup.getId()).build();
    }

    public Response modifyGroup(GroupRequest request, int id)
    {
        String error = validateRolesAndPermissions(request.roles,request.permissionLevels);
        if(StringUtils.isNoneEmpty(error))
        {
            throw new WebApplicationException(
                error,
                Response.Status.BAD_REQUEST);
        }

        Optional<Group> groupOptional = getGroup(id);
        if(groupOptional.isPresent())
        {
            Group group = groupOptional.get();
            group.addPermissionLevels(getPermissions(request.permissionLevels));
            group.addRoles(getRoles(request.roles));
            group.addUsers(getUsers(request.permissionLevels,request.roles));
            Group createdGroup = groupRepository.save(group);
            return Response.status(Response.Status.CREATED).entity(createdGroup.getId()).build();
        }
        else
        {
            throw new WebApplicationException(
                GroupErrorMessages.GROUP_DOES_NOT_EXIST,
                Response.Status.BAD_REQUEST);
        }
    }

    private String validateRequest(GroupRequest request) {

        if (!isGroupNameValid(request.name)
            || !InputValidator.isNameValid(request.name)) {
            return GroupErrorMessages.GROUP_NAME_INVALID;
        }

        String error = validateRolesAndPermissions(request.roles,request.permissionLevels);
        if(StringUtils.isNoneEmpty(error))
            return error;

        return StringUtils.EMPTY;
    }

    private String validateRolesAndPermissions(Set<Long> roles, Set<Long> permissions)
    {
        for (long roleID : roles) {
            if (!isRoleIdValid(roleID)) {
                return String.format(GroupErrorMessages.ROLE_ID_INVALID, roleID);
            }
        }

        if(permissions.size() < MINIMUM_AMOUNT_OF_PERMISSIONS)
        {
            return GroupErrorMessages.GROUP_PERMISSIONS_REQUIRED;
        }

        for (long permissionID : permissions) {
            if (!isPermissionValid(permissionID)) {
                return String.format(GroupErrorMessages.PERMISSION_ID_INVALID, permissionID);
            }
        }

        return StringUtils.EMPTY;
    }

    private Set<User> getUsers(Set<Long> permissionIDs, Set<Long> rolesIDs)
    {
        Set<User> userWithRole = new HashSet<>();
        Set<User> userWithPermissionLevel = new HashSet<>();

        permissionIDs.
            forEach(id -> userWithPermissionLevel.addAll(userRepository.findUsersWithPermissionLevel(id)));
        if(rolesIDs.isEmpty())
            return userWithPermissionLevel;
        else {
            rolesIDs.forEach(id -> userWithRole.addAll(userRepository.findUsersWithRole(id)));
            return Sets.intersection(userWithPermissionLevel,userWithRole);
        }
    }

    private Set<Role> getRoles(Set<Long> rolesIDs)
    {
        Set<Role> roles = new HashSet<>();
        rolesIDs.forEach(id -> roles.add(rolesRepository.find(id).get()));
        return roles;
    }

    private Set<PermissionLevel> getPermissions(Set<Long> permissionID)
    {
        Set<PermissionLevel> permissions = new HashSet<>();
        permissionID.forEach(id -> permissions.add(permissionsRepository.find(id).get()));
        return permissions;
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

    private Optional<Group> getGroup(int id)
    {
        return groupRepository.find(id);
    }
}
