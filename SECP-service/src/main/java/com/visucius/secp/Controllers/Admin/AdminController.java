package com.visucius.secp.Controllers.Admin;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.AppCreateDTO;
import com.visucius.secp.DTO.RolesOrPermissionDTO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.Role;
import com.visucius.secp.models.User;
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

public class AdminController {
    private final static Logger LOG = LoggerFactory.getLogger(AdminController.class);

    private final UserDAO userDAO;
    private final RolesDAO rolesDAO;
    private final PermissionDAO permissionDAO;

    private final String userErrorString = "user";
    private final String roleErrorString = "role";
    private final String permissionErrorString = "permission";


    public AdminController(UserDAO userDAO, RolesDAO rolesDAO, PermissionDAO permissionDAO) {
        this.userDAO = userDAO;
        this.rolesDAO = rolesDAO;
        this.permissionDAO = permissionDAO;
    }

    public void registerAdmin(String userID) {
        validateInput(userID, userErrorString);
        User user = getUserFromID(userID);

        if(user.getLoginRole().equals(LoginRole.ADMIN)) {
            LOG.warn("Admin registration failed. User has already been registered as admin.");
            throw new WebApplicationException(AdminErrorMessage.REGISTER_ADMIN_FAIL_DUPLICATE_ENTRY, Response.Status.BAD_REQUEST);
        }

        user.setLoginRole(LoginRole.ADMIN);
        userDAO.save(user);
        LOG.info("New admin created.");
    }

    public void removeAdmin(String userID) {
        validateInput(userID, userErrorString);
        User user = getUserFromID(userID);

        if (!user.getLoginRole().equals(LoginRole.ADMIN)) {
            LOG.warn("Delete admin failed. User is not an admin.");
            throw new WebApplicationException(AdminErrorMessage.DELETE_ADMIN_FAIL_INVALID_USER, Response.Status.NO_CONTENT);
        }

        user.setLoginRole(LoginRole.NORMAL);
        userDAO.save(user);
        LOG.info("An admin has been removed.");
    }

    public Response registerRoles(AppCreateDTO request) {
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

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    public Response registerPermissions(AppCreateDTO request) {
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

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    public Response deleteRole(String roleID) {
        validateInput(roleID, roleErrorString);
        Role role = getRoleFromID(roleID);

        if (!Util.isCollectionEmpty(role.getUsers()) || !Util.isCollectionEmpty(role.getGroups())) {
            String error = String.format(AdminErrorMessage.DELETE_ROLES_FAIL_ROLE_IN_USE, role.getRole());
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        rolesDAO.delete(role);
        return Response.status(Response.Status.OK).build();
    }

    public Response deletePermission(String permissionID) {
        validateInput(permissionID, permissionErrorString);
        Permission permission = getPermissionFromID(permissionID);

        if (!Util.isCollectionEmpty(permission.getUsers()) || !Util.isCollectionEmpty(permission.getGroups())) {
            String error = String.format(AdminErrorMessage.DELETE_PERMISSION_FAIL_PERMISSION_IN_USE, permission.getLevel());
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        permissionDAO.delete(permission);
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

        Optional<User> user = userDAO.find(id);

        if (!user.isPresent()) {
            String error = String.format(AdminErrorMessage.DOES_NOT_EXIST, userErrorString);
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }

        return user.get();
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
