package com.visucius.secp.Controllers.User;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.DeviceDTO;
import com.visucius.secp.DTO.SecretDTO;
import com.visucius.secp.daos.DeviceDAO;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Device;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Secret;
import com.visucius.secp.models.User;
import com.visucius.secp.util.Util;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceController {
    private GroupDAO groupDAO;
    private UserDAO userDAO;
    private DeviceDAO deviceDAO;

    public DeviceController(UserDAO userDAO, GroupDAO groupDAO, DeviceDAO deviceDAO) {
        this.groupDAO = groupDAO;
        this.deviceDAO = deviceDAO;
        this.userDAO = userDAO;
    }

    public Response addSecretKeyForDevices(Set<SecretDTO> secretDTOS) {
        if (Util.isCollectionEmpty(secretDTOS)) {
            throw new WebApplicationException(UserErrorMessage.SECRET_ADD_FAIL_NO_DEVICE, Response.Status.BAD_REQUEST);
        }

        validate(secretDTOS);
        addSecretKeyForDevice(secretDTOS);

        return Response.status(Response.Status.OK).build();
    }

    public Response getDeviceSecretForUser(User user, String deviceName) {
        if (StringUtils.isEmpty(deviceName)) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_GET_FAIL_NO_DEVICE_EXISTS, Response.Status.NO_CONTENT);
        }

        Device device = deviceDAO.findByDeviceName(deviceName);

        if(device == null) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_GET_FAIL_NO_DEVICE_EXISTS, Response.Status.NO_CONTENT);
        }

        List<Secret> secretsForUserDevice = deviceDAO.findSecretForUserDevice(user.getId(), device.getId());

        Set<SecretDTO> secretDTOS = new HashSet<>();
        for (Secret secret : secretsForUserDevice) {
            secretDTOS.add(new SecretDTO(secret));
        }

        return Response.status(Response.Status.OK).entity(secretDTOS).build();
    }

    public Response getDevicesForUser(long userID) {
        Optional<User> userOptional = userDAO.find(userID);

        if (!userOptional.isPresent()) {
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }

        return Response.status(Response.Status.OK).entity(getDevicesForUser(userOptional.get())).build();
    }

    private void addSecretKeyForDevice(Set<SecretDTO> secretDTOS) {

        for (SecretDTO secretDTO : secretDTOS) {
            Optional<Device> deviceOptional = deviceDAO.find(secretDTO.getDeviceID());
            if (deviceOptional.isPresent()) {
                Device device = deviceOptional.get();
                Secret secret = deviceDAO.findSecretByDeviceAndGroupID(secretDTO.getGroupID(), secretDTO.getDeviceID());

                if (secret != null) {
                    secret.setEncryptedSecret(secretDTO.getEncryptedSecret());
                } else {
                    secret = new Secret(secretDTO.getGroupID(), secretDTO.getUserID(), secretDTO.getEncryptedSecret(), device);
                }

                device.getGroupSecrets().add(secret);
                deviceDAO.save(device);
            }
        }

    }

    public Response getDevicesForGroup(long groupID) {
        Optional<Group> groupOptional = groupDAO.find(groupID);

        if (!groupOptional.isPresent()) {
            throw new WebApplicationException(UserErrorMessage.SECRET_ADD_FAIL_INVALID_GROUP, Response.Status.BAD_REQUEST);
        }

        Group group = groupOptional.get();

        Set<DeviceDTO> devices = new HashSet<>();

        //get all the devices for users
        Set<DeviceDTO> deviceDTOSForUsers = getDevicesForUser(group.getUsers());
        if (Util.isCollectionEmpty(deviceDTOSForUsers)) {
            throw new WebApplicationException(UserErrorMessage.DEVICE_GET_FAIL_NO_DEVICE_EXISTS, Response.Status.NO_CONTENT);
        }

        devices.addAll(deviceDTOSForUsers);
        //get all the devices for admins
        List<User> admins = userDAO.findAdmins();

        if (!Util.isCollectionEmpty(admins)) {
            devices.addAll(getDevicesForUser(new HashSet<User>(admins)));
        }

        return Response.status(Response.Status.OK).entity(devices).build();
    }

    private Set<DeviceDTO> getDevicesForUser(Set<User> users) {
        Set<DeviceDTO> devices = new HashSet<>();

        if(!Util.isCollectionEmpty(users)) {
            for (User user : users) {
                Set<DeviceDTO> deviceDTOS = getDevicesForUser(user);
                if (!Util.isCollectionEmpty(deviceDTOS)) {
                    devices.addAll(deviceDTOS);
                }
            }
        }

        return devices;
    }

    private Set<DeviceDTO> getDevicesForUser(User user) {
        Set<DeviceDTO> deviceDTOS = new HashSet<>();

        if (!Util.isCollectionEmpty(user.getDevices())) {
            for (Device device : user.getDevices()) {
                DeviceDTO deviceDTO = new DeviceDTO(device);
                deviceDTO.setUserID(user.getId());
                deviceDTOS.add(deviceDTO);
            }
        }

        return deviceDTOS;
    }

    private void validate(Set<SecretDTO> secretDTOS) {
        for (SecretDTO secretDTO : secretDTOS) {
            if (secretDTO == null) {
                throw new WebApplicationException(UserErrorMessage.SECRET_ADD_FAIL_NO_SECRET_INFO, Response.Status.BAD_REQUEST);
            }

            Optional<Group> groupOptional = groupDAO.find(secretDTO.getGroupID());

            if (!groupOptional.isPresent()) {
                throw new WebApplicationException(UserErrorMessage.SECRET_ADD_FAIL_INVALID_GROUP, Response.Status.BAD_REQUEST);
            }

            Optional<User> userOptional = userDAO.find(secretDTO.getUserID());

            if (!userOptional.isPresent()) {
                throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.BAD_REQUEST);
            }

            Optional<Device> deviceOptional = deviceDAO.find(secretDTO.getDeviceID());
            if (!deviceOptional.isPresent()) {
                throw new WebApplicationException(UserErrorMessage.SECRET_ADD_FAIL_INVALID_DEVICE, Response.Status.BAD_REQUEST);
            }

            if (StringUtils.isBlank(secretDTO.getEncryptedSecret())) {
                throw new WebApplicationException(UserErrorMessage.SECRET_ADD_FAIL_NO_SECRET_KEY, Response.Status.BAD_REQUEST);
            }
        }
    }
}
