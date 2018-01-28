package com.visucius.secp.Controllers.User;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.visucius.secp.DTO.DeviceDTO;
import com.visucius.secp.DTO.GroupDTO;
import com.visucius.secp.DTO.RolesOrPermissionDTO;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.daos.*;
import com.visucius.secp.models.*;
import com.visucius.secp.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserController {
    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
    private UserDAO userDAO;
    private DeviceDAO deviceDAO;
    private GroupDAO groupDAO;
    private PermissionDAO permissionDAO;
    private RolesDAO rolesDAO;

    public UserController(UserDAO userDAO, DeviceDAO deviceDAO, PermissionDAO permissionDAO,
                          RolesDAO rolesDAO, GroupDAO groupDAO) {
        this.userDAO = userDAO;
        this.deviceDAO = deviceDAO;
        this.rolesDAO = rolesDAO;
        this.permissionDAO = permissionDAO;
        this.groupDAO = groupDAO;
    }

    public Response getAllUsers() {
        List<User> users = userDAO.findAll();

        if(Util.isCollectionEmpty(users)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        Set<UserDTO> response = users.stream()
            .map(user -> {
                return getUserResponse(user);
            })
            .collect(Collectors.toSet());

        return Response.status(Response.Status.OK).entity(response).build();
    }

    public Response getUserGivenId(String id) {
        if(StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        long userID = Long.parseLong(id);
        User user = getUser(userID);

        return Response.status(Response.Status.OK).entity(getUserResponse(user)).build();
    }

    public Response deleteUser(String id)
    {
        if(StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        long userID = Long.parseLong(id);
        User user = getUser(userID);
        user.setIsActive(false);
        userDAO.save(user);
        return Response.status(Response.Status.OK).build();
    }

    public boolean isUserAnAdmin(String userID) {
        if(StringUtils.isBlank(userID) || !StringUtils.isNumeric(userID)) {
            LOG.warn("Empty user id provided.");
            return false;
        }

        long id = Long.parseLong(userID);
        Optional<User> user = userDAO.find(id);

        if (!user.isPresent()) {
            LOG.warn("User not found.");
            return false;
        }

        return user.get().getLoginRole().equals(LoginRole.ADMIN);
    }

    public Response addDevice(DeviceDTO deviceDTO) {
        validate(deviceDTO);

        String deviceName = deviceDTO.getDeviceName();

        Optional<User> dbUser = userDAO.find(deviceDTO.getUserID());
        if (!dbUser.isPresent()) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_ADD_FAIL_USER_NOT_FOUND, Response.Status.BAD_REQUEST);
        }

        Device device = deviceDAO.findByDeviceName(deviceName);
        if(device == null) {
            device = new Device(deviceName, deviceDTO.getPublicKey());
            deviceDAO.save(device);
        }

        List<Device> devicesForUser = deviceDAO.getDevicesForUser(deviceDTO.getUserID());
        if (devicesForUser == null || !devicesForUser.contains(device)) {
            //adding the device for the user
            User user = dbUser.get();
            user.addDevice(device);
            userDAO.save(user);
            LOG.info("New device added for user");
            return Response.status(Response.Status.CREATED).entity(device.getId()).build();
        }

        throw new WebApplicationException(UserErrorMessage.DEVICE_ADD_FAIL_DEVICE_EXISTS, Response.Status.BAD_REQUEST);
    }

    public boolean isDeviceRegisteredForUser(long userID, String deviceName)
    {
        List<Device> devicesForUser = deviceDAO.getDevicesForUser(userID);
        if (devicesForUser == null || devicesForUser.isEmpty()) {
            return false;
        }

        Set<String> deviceNamesForUser = devicesForUser.stream().map(Device::getName).collect(Collectors.toSet());
        return deviceDAO.findByDeviceName(deviceName) != null && deviceNamesForUser.contains(deviceName);
    }

    public Response getUsersPublicKeys(long userID) {
        Optional<User> dbUser = userDAO.find(userID);

        if (!dbUser.isPresent()) {
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }

        List<Device> devices = deviceDAO.getDevicesForUser(userID);
        if(devices == null || devices.isEmpty()) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_GET_FAIL_NO_DEVICE_EXISTS, Response.Status.NO_CONTENT);
        }

        Set<DeviceDTO> responseEntity = devices.stream()
            .filter(device -> !StringUtils.isEmpty(device.getPublicKey()))
            .map(device -> {return new DeviceDTO(device.getId(), device.getPublicKey());})
            .collect(Collectors.toSet());

        if(responseEntity.isEmpty()) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_GET_FAIL_NO_PUBLIC_KEY_EXISTS, Response.Status.NO_CONTENT);
        }

        return Response.status(Response.Status.OK).entity(responseEntity).build();
    }

    public Response modifyUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new WebApplicationException(UserErrorMessage.MODIFY_USER_FAIL_NO_USER_INFO,
                Response.Status.BAD_REQUEST);
        }

        validatePermission(userDTO.getPermission());
        validateRoles(userDTO.getRoles());

        User user = getUser(userDTO.getUserID());
        user.setPermission(getPermission(userDTO.getPermission()));
        user.setRoles(getRoles(userDTO.getRoles()));
        user.setIsActive(userDTO.isActive());
        user.setLoginRole(userDTO.getLoginRole());

        //adding user to groups
        user.setGroups(getUserGroups(user));

        userDAO.save(user);

        return Response.status(Response.Status.OK).entity(getUserResponse(user)).build();
    }

    public Set<Role> getRoles(Set<RolesOrPermissionDTO> rolesFromUserDTO)
    {
        Set<Role> roles = new HashSet<>();
        rolesFromUserDTO.forEach(role -> roles.add(rolesDAO.find(role.getId()).get()));
        return roles;
    }

    public Permission getPermission(RolesOrPermissionDTO permisionFromUserDTO)
    {
        return permissionDAO.find(permisionFromUserDTO.getId()).get();
    }

    private Set<Group> getUserGroups(User user) {
        //get user's new group
        Set<Group> groupsForUser = findGroupsForUser(user.getPermission(), user.getRoles()).stream()
            .map(group -> {
                group.getUsers().add(user);
                groupDAO.save(group);
                return group;
            }).collect(Collectors.toSet());

        //remove user from old group
        Set<Group> oldGroups = Sets.difference(user.getGroups(), groupsForUser);
        oldGroups.stream()
            .filter(group -> !groupsForUser.contains(group) && group.getGroupType().equals(GroupType.PUBLIC))
            .map(group -> {
                group.getUsers().remove(user);
                groupDAO.save(group);
                return group;
            }).collect(Collectors.toSet());

        return groupsForUser;
    }


    private Set<Group> findGroupsForUser(Permission permission, Set<Role> roles)
    {
        List<Long> roleIDS = roles.stream()
            .map(role -> { return role.getId(); }).collect(Collectors.toList());

        if (roleIDS.isEmpty())
            roleIDS.add(-1L);
        List<Group> groups = groupDAO.findGroupsForUser(permission.getId(), roleIDS);
        return new HashSet<>(groups);
    }

    private void validatePermission(RolesOrPermissionDTO permission)
    {
        if (permission == null) {
            throw new WebApplicationException(UserErrorMessage.MODIFY_USER_FAIL_PERMISSIONS_REQUIRED,
                Response.Status.BAD_REQUEST);
        }

        Optional<Permission> permissionFromDB = permissionDAO.find(permission.getId());
        if (!permissionFromDB.isPresent()) {
            String error = String.format(UserErrorMessage.MODIFY_USER_FAIL_PERMISSIONS_INVALID, permission.getName());
            throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
        }
    }

    private void validateRoles(Set<RolesOrPermissionDTO> roles)
    {
        if (!Util.isCollectionEmpty(roles)) {
            for (RolesOrPermissionDTO role : roles) {
                Optional<Role> roleFromDB = rolesDAO.find(role.getId());
                if (!roleFromDB.isPresent()) {
                    String error = String.format(UserErrorMessage.MODIFY_USER_FAIL_ROLES_INVALID, role.getName());
                    throw new WebApplicationException(error, Response.Status.BAD_REQUEST);
                }
            }
        }
    }

    private void validate(DeviceDTO deviceDTO) {
        if (deviceDTO == null) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_ADD_FAIL_NO_DEVICE_INFO, Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(deviceDTO.getDeviceName())) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_ADD_FAIL_NO_DEVICE_NAME, Response.Status.BAD_REQUEST);
        } else if (StringUtils.isBlank(deviceDTO.getPublicKey()) || deviceDTO.getPublicKey().length() < 100) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_ADD_FAIL_INVALID_PUBLIC_KEY, Response.Status.BAD_REQUEST);
        }
    }


    private User getUser(long userID)
    {
        Optional<User> userOptional = userDAO.find(userID);
        if (!userOptional.isPresent()) {
            throw new WebApplicationException(
                UserErrorMessage.USER_ID_INVALID,
                Response.Status.NO_CONTENT);
        }

        return userOptional.get();
    }

    private UserDTO getUserResponse(User user) {
        UserDTO userDTO = new UserDTO(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstname());
        userDTO.setLastName(user.getLastname());
        userDTO.setNumOfRoles(user.getRoles().size());
        userDTO.setActive(user.isActive());
        userDTO.setLoginRole(user.getLoginRole());

        Set<GroupDTO> groups = getGroupsForUser(user).stream().filter(group -> group.isActive() && group.getGroupType().equals(GroupType.PUBLIC))
            .map(group -> {
                return new GroupDTO(group.getId());
            }).collect(Collectors.toSet());

        Set<RolesOrPermissionDTO> roles = user.getRoles().stream()
            .map(role -> {
                return new RolesOrPermissionDTO(role.getId(), role.getRole(), role.getColor());
            }).collect(Collectors.toSet());

        Permission userPermission = user.getPermission();

        //setting user permission
        RolesOrPermissionDTO permissionForUser = new RolesOrPermissionDTO();
        if (userPermission != null) {
            permissionForUser.setId(userPermission.getId());
            permissionForUser.setName(userPermission.getLevel());
            permissionForUser.setColor(userPermission.getColor());
        }

        userDTO.setPermission(permissionForUser);
        userDTO.setRoles(roles);
        userDTO.setNumOfGroups(groups.size());
        userDTO.setGroups(groups);

        return userDTO;
    }

    private Set<Group> getGroupsForUser(User user) {
        Set<Group> groups = new HashSet<>();
        Util.addAllIfNotNull(groups, user.getGroups());

        return groups;
    }
}
