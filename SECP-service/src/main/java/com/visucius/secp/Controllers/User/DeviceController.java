package com.visucius.secp.Controllers.User;

import com.google.common.base.Optional;
import com.visucius.secp.DTO.SecretDTO;
import com.visucius.secp.daos.DeviceDAO;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Device;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.Secret;
import com.visucius.secp.util.Util;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Set;

public class DeviceController {
    private GroupDAO groupDAO;
    private DeviceDAO deviceDAO;

    public DeviceController(UserDAO userDAO, GroupDAO groupDAO, DeviceDAO deviceDAO) {
        this.groupDAO = groupDAO;
        this.deviceDAO = deviceDAO;
    }

    public Response addSecretGroupForDevices(Set<SecretDTO> secretDTOS) {
        if (Util.isCollectionEmpty(secretDTOS)) {
            throw new WebApplicationException(UserErrorMessage.SECRET_ADD_FAIL_NO_DEVICE, Response.Status.BAD_REQUEST);
        }

        validate(secretDTOS);
        addSecretKeyForDevice(secretDTOS);

        return Response.status(Response.Status.OK).build();
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
                    secret = new Secret(secretDTO.getGroupID(), secretDTO.getEncryptedSecret(), device);
                }

                device.getGroupSecrets().add(secret);
                deviceDAO.save(device);
            }
        }

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
