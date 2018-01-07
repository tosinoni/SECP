package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.visucius.secp.DTO.*;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.*;
import com.visucius.secp.util.InputValidator;
import com.visucius.secp.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
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

        return Response.status(Response.Status.CREATED).entity(getGroupResponse(createdGroup)).build();
    }
    public Response deletePermissions(GroupModifyRequest request, int groupID)
    {
        Group group = getGroup(groupID);
        String error = validateDeletePermissionRequest(request, group);
        if (StringUtils.isNoneEmpty(error)) {
            throw new WebApplicationException(
                error,
                Response.Status.BAD_REQUEST);
        }
        group.removePermissions(getPermissions(request.permissions));
        return updateGroup(group);

    }
    public Response deleteRoles(GroupModifyRequest request, int groupID)
    {
        Group group = getGroup(groupID);
        String error = validateDeleteRolesRequest(request, group);
        if (StringUtils.isNoneEmpty(error)) {
            throw new WebApplicationException(
                error,
                Response.Status.BAD_REQUEST);
        }

        group.removeRoles(getRoles(request.roles));
        return updateGroup(group);
    }

    public Response addPermissionsToGroup(GroupModifyRequest request, int groupID)
    {
        Group group = getGroup(groupID);
        String error = validateAddPermissionsRequest(request, group);
        if (StringUtils.isNoneEmpty(error)) {
            throw new WebApplicationException(
                error,
                Response.Status.BAD_REQUEST);
        }

        group.addPermissions(getPermissions(request.permissions));
        return updateGroup(group);
    }

    public Response addRolesToGroup(GroupModifyRequest request, int groupID) {

        Group group = getGroup(groupID);
        String error = validateAddRolesRequest(request, group);
        if (StringUtils.isNoneEmpty(error)) {
            throw new WebApplicationException(
                error,
                Response.Status.BAD_REQUEST);
        }


        group.addRoles(getRoles(request.roles));
        return updateGroup(group);
    }

    public Response updateGroup(Group group)
    {
        group.setUsers(this.findUsersWithRolesAndPermissions(group.getRoles(), group.getPermissions()));
        Group createdGroup = groupRepository.save(group);
        return Response.status(Response.Status.CREATED).entity(getGroupResponse(createdGroup)).build();
    }

    private Group getGroup(long groupID)
    {
        Optional<Group> groupOptional = groupRepository.find(groupID);
        if (!groupOptional.isPresent()) {
            throw new WebApplicationException(
                GroupErrorMessages.GROUP_DOES_NOT_EXIST,
                Response.Status.BAD_REQUEST);
        }

        return groupOptional.get();
    }

    public Response getAllGroups() {
        List<Group> groups = groupRepository.findAll();

        if(Util.isCollectionEmpty(groups)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        Set<GroupDTO> response = groups.stream()
            .map(group -> {
                return getGroupResponse(group);
            })
            .collect(Collectors.toSet());

        return Response.status(Response.Status.OK).entity(response).build();
    }

    public Response getGroupGivenId(String id) {
        if(StringUtils.isEmpty(id) ) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        long groupID = Long.parseLong(id);
        Group group = getGroup(groupID);

        return Response.status(Response.Status.OK).entity(getGroupResponse(group)).build();
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


    private String validateDeleteRolesRequest(GroupModifyRequest request, Group currentGroup)
    {
        Set<Long> currentRolesIDs = currentGroup.getRoles().
            stream().
            map(Role::getId).
            collect(Collectors.toSet());
        for (long roleId : request.roles) {

            if (!currentRolesIDs.contains(roleId))
                return String.format(GroupErrorMessages.ROLE_ID_INVALID, roleId);
        }

        return StringUtils.EMPTY;
    }

    private String validateDeletePermissionRequest(GroupModifyRequest request, Group currentGroup)
    {
        Set<Long> currentPermissionIDs = currentGroup.getPermissions().
            stream().
            map(Permission::getId).
            collect(Collectors.toSet());

        for (long permissionId : request.permissions) {

            if (!currentPermissionIDs.contains(permissionId))
                return String.format(GroupErrorMessages.PERMISSION_ID_INVALID, permissionId);

        }

        if((currentPermissionIDs.size() -  request.permissions.size())
            < MINIMUM_AMOUNT_OF_PERMISSIONS)
        {
            return GroupErrorMessages.GROUP_PERMISSIONS_REQUIRED;
        }

        return StringUtils.EMPTY;
    }

    private String validateAddPermissionsRequest(GroupModifyRequest request, Group currentGroup)
    {
        String permissionError = validatePermissions(request.permissions);
        if (StringUtils.isNoneEmpty(permissionError))
            return permissionError;

        return StringUtils.EMPTY;
    }

    private String validateAddRolesRequest(GroupModifyRequest request, Group currentGroup)
    {

        String roleError = validateRoles(request.roles);
        if (StringUtils.isNoneEmpty(roleError))
            return roleError;

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

    private GroupDTO getGroupResponse (Group group) {
        GroupDTO groupDTO = new GroupDTO(group.getId());
        groupDTO.setName(group.getName());
        groupDTO.setNumOfPermissions(group.getPermissions().size());
        groupDTO.setNumOfRoles(group.getRoles().size());
        groupDTO.setNumOfUsers(group.getUsers().size());

        Set<UserDTO> users = getUsersInGroup(group).stream()
            .map(user -> {
                return new UserDTO(user.getId(), getDevicesForUser(user));
            }).collect(Collectors.toSet());

        Set<RolesOrPermissionDTO> roles = group.getRoles().stream()
            .map(role -> {
                return new RolesOrPermissionDTO(role.getId(), role.getRole());
            }).collect(Collectors.toSet());

        Set<RolesOrPermissionDTO> permissions = group.getPermissions().stream()
            .map(permission -> {
                return new RolesOrPermissionDTO(permission.getId(), permission.getLevel());
            }).collect(Collectors.toSet());

        groupDTO.setUsers(users);
        groupDTO.setRoles(roles);
        groupDTO.setPermissions(permissions);

        return groupDTO;
    }

    private Set<User> getUsersInGroup(Group group) {
        Set<User> users = new HashSet<>();
        Util.addAllIfNotNull(users, group.getUsers());
        Util.addAllIfNotNull(users, userRepository.findAdmins());

        return users;
    }

    private Set<DeviceDTO> getDevicesForUser(User user) {
        //return devices that have a public key
        return user.getDevices().stream()
            .filter(device -> !StringUtils.isEmpty(device.getPublicKey()))
            .map(device -> {
                return new DeviceDTO(device.getId(), device.getPublicKey());
            }).collect(Collectors.toSet());
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
