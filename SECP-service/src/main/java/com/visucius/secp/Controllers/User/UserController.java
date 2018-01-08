package com.visucius.secp.Controllers.User;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.DeviceDTO;
import com.visucius.secp.DTO.GroupDTO;
import com.visucius.secp.DTO.RolesOrPermissionDTO;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.daos.DeviceDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Device;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.LoginRole;
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

public class UserController {
    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);
    private UserDAO userDAO;
    private DeviceDAO deviceDAO;


    public UserController(UserDAO userDAO, DeviceDAO deviceDAO) {
        this.userDAO = userDAO;
        this.deviceDAO = deviceDAO;
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
        userDTO.setNumOfPermissions(user.getPermissions().size());
        userDTO.setNumOfRoles(user.getRoles().size());
        userDTO.setNumOfGroups(user.getGroups().size());

        Set<GroupDTO> groups = getGroupsForUser(user).stream()
            .map(group -> {
                return new GroupDTO(group.getId());
            }).collect(Collectors.toSet());

        Set<RolesOrPermissionDTO> roles = user.getRoles().stream()
            .map(role -> {
                return new RolesOrPermissionDTO(role.getId(), role.getRole());
            }).collect(Collectors.toSet());

        Set<RolesOrPermissionDTO> permissions = user.getPermissions().stream()
            .map(permission -> {
                return new RolesOrPermissionDTO(permission.getId(), permission.getLevel());
            }).collect(Collectors.toSet());

        userDTO.setGroups(groups);
        userDTO.setRoles(roles);
        userDTO.setPermissions(permissions);

        return userDTO;
    }

    private Set<Group> getGroupsForUser(User user) {
        Set<Group> groups = new HashSet<>();
        Util.addAllIfNotNull(groups, user.getGroups());

        return groups;
    }
}
