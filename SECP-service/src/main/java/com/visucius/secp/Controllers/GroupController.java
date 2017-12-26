package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.visucius.secp.DTO.GroupCreateRequest;
import com.visucius.secp.DTO.GroupModifyRequest;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Permission;
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
import java.util.stream.Collectors;

public class GroupController {

    private static final Logger LOG = LoggerFactory.getLogger(GroupController.class);
    public static final int MINIMUM_AMOUNT_OF_PERMISSIONS = 1;

    private GroupDAO groupRepository;
    private UserDAO userRepository;
    private RolesDAO rolesRepository;
    private PermissionDAO permissionsRepository;

    public GroupController(GroupDAO groupRepository,
                           UserDAO userRepository,
                           RolesDAO rolesRepository,
                           PermissionDAO permissionDAO)
    {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.permissionsRepository = permissionDAO;
    }


    public Response createGroup(GroupCreateRequest request)
    {

        String error = validateCreateRequest(request);
        if(StringUtils.isNoneEmpty(error))
        {
                throw new WebApplicationException(
                    error,
                    Response.Status.BAD_REQUEST);
        }

        Group group = new Group(request.name);
        group.setRoles(getRoles(request.roles));
        group.setUsers(findUsersWithRoleIDsAndPermissionIDs(request.permissions,request.roles));
        group.setPermissions(getPermissions(request.permissions));
        Group createdGroup = groupRepository.save(group);

        return Response.status(Response.Status.CREATED).entity(createdGroup.getId()).build();
    }

    public Response modifyGroup(GroupModifyRequest request, int id) {

        Optional<Group> groupOptional = getGroup(id);
        if (!groupOptional.isPresent()) {
            throw new WebApplicationException(
                GroupErrorMessages.GROUP_DOES_NOT_EXIST,
                Response.Status.BAD_REQUEST);
        }

        Group group = groupOptional.get();
        String error = validateModifyReqeuest(request, group);
        if (StringUtils.isNoneEmpty(error)) {
            throw new WebApplicationException(
                error,
                Response.Status.BAD_REQUEST);
        }


        group.addPermissions(getPermissions(request.add_permissions));
        group.addRoles(getRoles(request.add_roles));

        group.removePermissions(getPermissions(request.remove_permissions));
        group.removeRoles(getRoles(request.remove_roles));

        group.setUsers(this.findUsersWithRolesAndPermissions(group.getRoles(), group.getPermissions()));
        Group createdGroup = groupRepository.save(group);
        return Response.status(Response.Status.CREATED).entity(createdGroup.getId()).build();


    }

    private String validateCreateRequest(GroupCreateRequest request) {

        if (!isGroupNameValid(request.name)
            || !InputValidator.isNameValid(request.name)) {
            return GroupErrorMessages.GROUP_NAME_INVALID;
        }

        if(request.permissions.size() < MINIMUM_AMOUNT_OF_PERMISSIONS)
        {
            return GroupErrorMessages.GROUP_PERMISSIONS_REQUIRED;
        }

        String roleError = validateRoles(request.roles);
        if(StringUtils.isNoneEmpty(roleError))
            return roleError;

        String permissionsError = validatePermissions(request.permissions);
        if(StringUtils.isNoneEmpty(permissionsError))
            return permissionsError;

        return StringUtils.EMPTY;
    }

    private String validateModifyReqeuest(GroupModifyRequest request, Group currentGroup) {
        Set<Long> allPermissions = Sets.intersection(request.add_permissions, request.remove_permissions);
        Set<Long> allRoles = Sets.intersection(request.add_roles, request.remove_roles);

        if (!allPermissions.isEmpty())
            return GroupErrorMessages.GROUP_MODIFY_OVERLAP_PERMISSIONS;
        if (!allRoles.isEmpty())
            return GroupErrorMessages.GROUP_MODIFY_OVERLAP_ROLES;

        Set<Long> currentRolesIDs = currentGroup.getRoles().
            stream().
            map(Role::getId).
            collect(Collectors.toSet());
        for (long roleId : request.remove_roles) {

            if (!currentRolesIDs.contains(roleId))
                return String.format(GroupErrorMessages.ROLE_ID_INVALID, roleId);

        }

        Set<Long> currentPermissionIDs = currentGroup.getPermissions().
            stream().
            map(Permission::getId).
            collect(Collectors.toSet());

        for (long permissionId : request.remove_permissions) {

            if (!currentPermissionIDs.contains(permissionId))
                return String.format(GroupErrorMessages.PERMISSION_ID_INVALID, permissionId);

        }

        String roleError = validateRoles(request.add_roles);
        if (StringUtils.isNoneEmpty(roleError))
            return roleError;

        String permissionError = validatePermissions(request.add_permissions);
        if (StringUtils.isNoneEmpty(permissionError))
            return permissionError;

        if((request.add_permissions.size() + currentPermissionIDs.size() -  request.remove_permissions.size())
            < MINIMUM_AMOUNT_OF_PERMISSIONS)
        {
            return GroupErrorMessages.GROUP_PERMISSIONS_REQUIRED;
        }

        return StringUtils.EMPTY;
    }

    private String validateRoles(Set<Long> roles)
    {
        for (long roleID : roles) {
            if (!isRoleIdValid(roleID)) {
                return String.format(GroupErrorMessages.ROLE_ID_INVALID, roleID);
            }
        }

        return StringUtils.EMPTY;
    }

    private String validatePermissions(Set<Long> permissions)
    {
        for (long permissionID : permissions) {
            if (!isPermissionValid(permissionID)) {
                return String.format(GroupErrorMessages.PERMISSION_ID_INVALID, permissionID);
            }
        }

        return StringUtils.EMPTY;
    }

    private Set<User> findUsersWithRoleIDsAndPermissionIDs(Set<Long> permissionIDs, Set<Long> rolesIDs)
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

    private Set<User> findUsersWithRolesAndPermissions(Set<Role> roles, Set<Permission> permissions)
    {
        Set<User> userWithRole = new HashSet<>();
        Set<User> userWithPermissionLevel = new HashSet<>();

        permissions.
            forEach(permission -> userWithPermissionLevel.addAll(permission.getUsers()));
        if(roles.isEmpty())
            return userWithPermissionLevel;
        else {
            roles.forEach(role -> userWithRole.addAll(role.getUsers()));
            return Sets.intersection(userWithPermissionLevel,userWithRole);
        }
    }

    private Set<Role> getRoles(Set<Long> rolesIDs)
    {
        Set<Role> roles = new HashSet<>();
        rolesIDs.forEach(id -> roles.add(rolesRepository.find(id).get()));
        return roles;
    }

    private Set<Permission> getPermissions(Set<Long> permissionID)
    {
        Set<Permission> permissions = new HashSet<>();
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
