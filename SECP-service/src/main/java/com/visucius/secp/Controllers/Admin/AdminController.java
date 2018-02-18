package com.visucius.secp.Controllers.Admin;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.visucius.secp.DTO.*;
import com.visucius.secp.daos.*;
import com.visucius.secp.models.*;
import com.visucius.secp.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private final UserDAO userDAO;
    private final GroupDAO groupDAO;
    private final RolesDAO rolesDAO;
    private final PermissionDAO permissionDAO;
    private final RecordsDAO recordsDAO;


    private final String userErrorString = "user";
    private final String groupErrorString = "group";
    private final String roleErrorString = "role";
    private final String permissionErrorString = "permission";


    public AdminController(UserDAO userDAO, RolesDAO rolesDAO,
                           PermissionDAO permissionDAO, GroupDAO groupDAO,
                           RecordsDAO recordsDAO) {
        this.userDAO = userDAO;
        this.rolesDAO = rolesDAO;
        this.permissionDAO = permissionDAO;
        this.groupDAO = groupDAO;
        this.recordsDAO = recordsDAO;
    }

    public void registerAdmin(User requestUser, String userID) {
        validateInput(userID, userErrorString);
        User user = getUserFromID(userID);

        if(user.getLoginRole().equals(LoginRole.ADMIN)) {
            LOG.warn("Admin registration failed. User has already been registered as admin.");
            throw new WebApplicationException(AdminErrorMessage.REGISTER_ADMIN_FAIL_DUPLICATE_ENTRY, Response.Status.BAD_REQUEST);
        }

        user.setLoginRole(LoginRole.ADMIN);
        userDAO.save(user);
        LOG.info("New admin created.");

        //adding the record to ledger
        String action = user.getFirstname() + " " + user.getLastname() + " was made an admin";
        recordsDAO.save(Util.createRecord(requestUser, ActionType.ADMIN, action));
    }

    public void removeAdmin(User requestUser, String userID) {
        validateInput(userID, userErrorString);
        User user = getUserFromID(userID);

        if (!user.getLoginRole().equals(LoginRole.ADMIN)) {
            LOG.warn("Delete admin failed. User is not an admin.");
            throw new WebApplicationException(AdminErrorMessage.DELETE_ADMIN_FAIL_INVALID_USER, Response.Status.NO_CONTENT);
        }

        user.setLoginRole(LoginRole.NORMAL);
        userDAO.save(user);
        LOG.info("An admin has been removed.");

        //adding the record to ledger
        String action = user.getFirstname() + " " + user.getLastname() + " was removed as an admin";
        recordsDAO.save(Util.createRecord(requestUser, ActionType.ADMIN, action));
    }

    public Response registerRoles(User requestUser, AppCreateDTO request) {
        String error = validateCreateRolesRequest(request);
        if(StringUtils.isNoneEmpty(error))
        {
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        Set<RolesOrPermissionDTO> response = new HashSet<>();
        for (String name : request.getRoles()) {
            Role role = rolesDAO.save(new Role(name, request.getColor()));
            response.add(new RolesOrPermissionDTO(role.getId(), role.getRole(), role.getColor()));
        }

        //adding the record to ledger
        String action = "Role " + StringUtils.join(request.getRoles(), ",") +  " was created";
        recordsDAO.save(Util.createRecord(requestUser, ActionType.ROLE, action));

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    public Response registerPermissions(User requestUser, AppCreateDTO request) {
        String error = validateCreatePermissionsRequest(request);
        if(StringUtils.isNoneEmpty(error))
        {
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        Set<RolesOrPermissionDTO> response = new HashSet<>();
        for (String name : request.getPermissions()) {
            Permission permission = permissionDAO.save(new Permission(name, request.getColor()));
            response.add(new RolesOrPermissionDTO(permission.getId(), permission.getLevel(), permission.getColor()));
        }


        //adding the record to ledger
        String action = "Permissions " + StringUtils.join(request.getPermissions(), ",") +  " was created";
        recordsDAO.save(Util.createRecord(requestUser, ActionType.PERMISSION, action));

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    public Response updateRoles(User requestUser, RolesOrPermissionDTO request, String id) {
        if(StringUtils.isEmpty(request.getColor()))
        {
            throw new WebApplicationException(AdminErrorMessage.REGISTER_ROLES_FAIL_INVALID_COLOR, Response.Status.BAD_REQUEST);
        }

        Role role = getRoleFromID(id);
        String previousColor = role.getColor();


        role.setColor(request.getColor());
        Role savedRole = rolesDAO.save(role);

        //adding the record to ledger
        String action = "Color of Role " + savedRole.getRole() +  " was changed from "
            + previousColor + " to " + savedRole.getColor() ;
        recordsDAO.save(Util.createRecord(requestUser, ActionType.ROLE, action));

        return Response.status(Response.Status.CREATED).
            entity(new RolesOrPermissionDTO(savedRole.getId(),savedRole.getRole(),savedRole.getColor())).build();
    }

    public Response updatePermissions(User requestUser, RolesOrPermissionDTO request, String id) {
        if(StringUtils.isEmpty(request.getColor()))
        {
            throw new WebApplicationException(AdminErrorMessage.REGISTER_ROLES_FAIL_INVALID_COLOR, Response.Status.BAD_REQUEST);
        }

        Permission permission = getPermissionFromID(id);
        String previousColor = permission.getColor();

        permission.setColor(request.getColor());
        Permission savedPermission = permissionDAO.save(permission);


        //adding the record to ledger
        String action = "Color of Permission " + savedPermission.getLevel()
            +  " was changed from " + previousColor + " to " + savedPermission.getColor() ;
        recordsDAO.save(Util.createRecord(requestUser, ActionType.PERMISSION, action));

        return Response.status(Response.Status.CREATED).
            entity(new RolesOrPermissionDTO(savedPermission.getId(),savedPermission.getLevel(),savedPermission.getColor())).build();
    }

    public Response deleteRole(User requestUser, String roleID) {
        validateInput(roleID, roleErrorString);
        Role role = getRoleFromID(roleID);

        if (!Util.isCollectionEmpty(role.getUsers()) || !Util.isCollectionEmpty(role.getGroups())) {
            String error = String.format(AdminErrorMessage.DELETE_ROLES_FAIL_ROLE_IN_USE, role.getRole());
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        rolesDAO.delete(role);

        //adding the record to ledger
        String action = "Role " + role.getRole() + " was deleted";
        recordsDAO.save(Util.createRecord(requestUser, ActionType.ROLE, action));

        return Response.status(Response.Status.OK).build();
    }

    public Response deletePermission(User requestUser, String permissionID) {
        validateInput(permissionID, permissionErrorString);
        Permission permission = getPermissionFromID(permissionID);

        if (!Util.isCollectionEmpty(permission.getUsers()) || !Util.isCollectionEmpty(permission.getGroups())) {
            String error = String.format(AdminErrorMessage.DELETE_PERMISSION_FAIL_PERMISSION_IN_USE, permission.getLevel());
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        permissionDAO.delete(permission);

        //adding the record to ledger
        String action = "Permission " + permission.getLevel() + " was deleted";
        recordsDAO.save(Util.createRecord(requestUser, ActionType.PERMISSION, action));

        return Response.status(Response.Status.OK).build();
    }

    public Response getAllRoles() {
        List<Role> roles = rolesDAO.findAll();

        if(Util.isCollectionEmpty(roles)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        Set<RolesOrPermissionDTO> response = roles.stream()
            .map(role -> {return new RolesOrPermissionDTO(role.getId(), role.getRole(), role.getColor());})
            .collect(Collectors.toSet());

        return Response.status(Response.Status.OK).entity(response).build();
    }

    public Response getAllPermissions() {
        List<Permission> permissions = permissionDAO.findAll();

        if(Util.isCollectionEmpty(permissions)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        Set<RolesOrPermissionDTO> response = permissions.stream()
            .map(permission -> {return new RolesOrPermissionDTO(permission.getId(), permission.getLevel(), permission.getColor());})
            .collect(Collectors.toSet());

        return Response.status(Response.Status.OK).entity(response).build();
    }

    public Response getLedger() {
        List<Record> allRecords = recordsDAO.findAll();

        if(Util.isCollectionEmpty(allRecords)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        Set<RecordsDTO> ledger = allRecords.stream()
            .map(record -> {return new RecordsDTO(record);})
            .collect(Collectors.toSet());

        return Response.status(Response.Status.OK).entity(ledger).build();
    }


    public Response getGroupsAudit(User requestUser, AuditDTO groupsAuditDTO) {
        validateGroupsAuditDTO(groupsAuditDTO);

        Set<String> groupNames = new HashSet<>();

        Set<GroupDTO> groupDTOS = groupsAuditDTO.getGroups().stream()
            .map(groupDTO -> {
                Group group = getGroupFromID(groupDTO.getGroupID());
                if(Util.isCollectionEmpty(group.getMessages())) {
                    return null;
                }

                groupNames.add(group.getName());
                return new GroupDTO(group);
            }).collect(Collectors.toSet());

        //remove all null elements from set
        groupDTOS.removeAll(Collections.singleton(null));

        if(groupDTOS.isEmpty()) {
            throw new WebApplicationException(AdminErrorMessage.AUDIT_GROUP_FAIL_NO_CONVERSATIONS, Response.Status.BAD_REQUEST);
        }

        //adding the record to ledger
        String action = "Group " + StringUtils.join(groupNames, ",") + " was audited";
        recordsDAO.save(Util.createRecord(requestUser, ActionType.AUDIT, action));

        return Response.status(Response.Status.OK).entity(groupDTOS).build();
    }

    public Response getUserAudit(User user, AuditDTO userAuditDTO) {
        validateUserAuditDTO(user, userAuditDTO);
        Set<Group> privateGroupsToBeAudited = getPrivateGroupsToBeAuditedForUser(userAuditDTO);

        if(privateGroupsToBeAudited.isEmpty()) {
            throw new WebApplicationException(AdminErrorMessage.AUDIT_USER_FAIL_NO_CONVERSATIONS, Response.Status.BAD_REQUEST);
        }

        Set<GroupDTO> groupDTOS = privateGroupsToBeAudited.stream().map(group -> {
            return new GroupDTO(group);
        }).collect(Collectors.toSet());

        //adding the record to ledger
        String action = "User " + userAuditDTO.getFromUser().getFirstName() + " "
            + userAuditDTO.getFromUser().getLastName() + " was audited";
        recordsDAO.save(Util.createRecord(user, ActionType.AUDIT, action));

        return Response.status(Response.Status.OK).entity(groupDTOS).build();
    }

    private Set<Group> getPrivateGroupsToBeAuditedForUser(AuditDTO userAuditDTO) {
        Set<Group> fromUserPrivateGroups = getUserPrivateGroups(userAuditDTO.getFromUser().getUserID());

        if(!Util.isCollectionEmpty(userAuditDTO.getToUsers()) && !fromUserPrivateGroups.isEmpty()) {
            Set<Group> commonGroups = new HashSet<>();

            for (UserDTO userDTO : userAuditDTO.getToUsers()) {
                Set<Group> toUserPrivateGroups = getUserPrivateGroups(userDTO.getUserID());
                Set<Group> commonGroup = Sets.intersection(fromUserPrivateGroups, toUserPrivateGroups);
                Util.addAllIfNotNull(commonGroups, commonGroup);
            }

            return commonGroups;
        }

        return fromUserPrivateGroups;
    }

    private Set<Group> getUserPrivateGroups(long userID) {
        User user = getUserFromID(userID);

        return  user.getGroups().stream()
            .filter(group -> group.getGroupType().equals(GroupType.PRIVATE) && !group.getMessages().isEmpty())
            .collect(Collectors.toSet());
    }

    private void validateUserAuditDTO(User user, AuditDTO userAuditDTO) {
        if(userAuditDTO == null || userAuditDTO.getFromUser() == null) {
            throw new WebApplicationException(AdminErrorMessage.AUDIT_USER_FAIL_EMPTY_REQUEST, Response.Status.BAD_REQUEST);
        }

        User fromUser = getUserFromID(userAuditDTO.getFromUser().getUserID());

        if(user.getId() == fromUser.getId()) {
            throw new WebApplicationException(AdminErrorMessage.AUDIT_USER_FAIL_INVALID_FROM_USER, Response.Status.BAD_REQUEST);
        }

        //validate the user ids of toUSers
        if(!Util.isCollectionEmpty(userAuditDTO.getToUsers())) {
            for (UserDTO userDTO : userAuditDTO.getToUsers())
                getUserFromID(userDTO.getUserID());
        }
    }

    private void validateGroupsAuditDTO(AuditDTO groupsAuditDTO) {
        if(groupsAuditDTO == null || groupsAuditDTO.getGroups().isEmpty()) {
            throw new WebApplicationException(AdminErrorMessage.AUDIT_GROUP_FAIL_EMPTY_REQUEST, Response.Status.BAD_REQUEST);
        }

        //validate the group ids of groups to audit
        for (GroupDTO groupDTO : groupsAuditDTO.getGroups())
            getGroupFromID(groupDTO.getGroupID());

    }

    private String validateCreateRolesRequest(AppCreateDTO request) {
        if (request == null || Util.isCollectionEmpty(request.getRoles())) {
            return  AdminErrorMessage.REGISTER_ROLES_FAIL_EMPTY_REQUEST;
        }

        else if(StringUtils.isEmpty(request.getColor())){
            return  AdminErrorMessage.REGISTER_ROLES_FAIL_INVALID_COLOR;
        }

        for (String role : request.getRoles()) {
            if (rolesDAO.findByName(role) != null) {
                return String.format(AdminErrorMessage.REGISTER_ROLES_FAIL_NAME_EXISTS, role);
            }
        }

        return StringUtils.EMPTY;
    }

    private String validateCreatePermissionsRequest(AppCreateDTO request) {
        if (request == null || Util.isCollectionEmpty(request.getPermissions())) {
            return  AdminErrorMessage.REGISTER_PERMISSIONS_FAIL_EMPTY_REQUEST;
        }

        else if(StringUtils.isEmpty(request.getColor())){
            return  AdminErrorMessage.REGISTER_PERMISSIONS_FAIL_INVALID_COLOR;
        }

        for (String name : request.getPermissions()) {
            if (permissionDAO.findByName(name) != null) {
                return String.format(AdminErrorMessage.REGISTER_PERMISSIONS_FAIL_NAME_EXISTS, name);
            }
        }

        return StringUtils.EMPTY;
    }

    private void validateInput(String userID, String type) {
        if(StringUtils.isBlank(userID) || !StringUtils.isNumeric(userID)) {
            String error = String.format(AdminErrorMessage.INVALID_ID, type);
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }
    }

    private User getUserFromID(String userID) {
        long id = Long.parseLong(userID);

        return getUserFromID(id);
    }

    private User getUserFromID(long id) {
        Optional<User> user = userDAO.find(id);

        if (!user.isPresent()) {
            String error = String.format(AdminErrorMessage.DOES_NOT_EXIST, userErrorString);
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        return user.get();
    }

    private Group getGroupFromID(long id) {
        Optional<Group> group = groupDAO.find(id);

        if (!group.isPresent()) {
            String error = String.format(AdminErrorMessage.DOES_NOT_EXIST, groupErrorString);
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        return group.get();
    }

    private Role getRoleFromID(String roleID) {
        long id = Long.parseLong(roleID);

        Optional<Role> role = rolesDAO.find(id);

        if (!role.isPresent()) {
            String error = String.format(AdminErrorMessage.DOES_NOT_EXIST, roleErrorString);
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        return role.get();
    }

    private Permission getPermissionFromID(String permissionID) {
        long id = Long.parseLong(permissionID);

        Optional<Permission> permission = permissionDAO.find(id);

        if (!permission.isPresent()) {
            String error = String.format(AdminErrorMessage.DOES_NOT_EXIST, permissionErrorString);
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        return permission.get();
    }
}
