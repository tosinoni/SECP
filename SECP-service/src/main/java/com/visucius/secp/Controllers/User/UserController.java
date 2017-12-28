package com.visucius.secp.Controllers.User;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.DeviceDTO;
import com.visucius.secp.daos.DeviceDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Device;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
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

    private void validate(DeviceDTO deviceDTO) {
        if (deviceDTO == null) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_ADD_FAIL_NO_DEVICE_INFO, Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(deviceDTO.getDeviceName())) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_ADD_FAIL_NO_DEVICE_NAME, Response.Status.BAD_REQUEST);
        } else if (StringUtils.isBlank(deviceDTO.getPublicKey())) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_ADD_FAIL_NO_DEVICE_PUBLIC_KEY, Response.Status.BAD_REQUEST);
        }
    }
}
