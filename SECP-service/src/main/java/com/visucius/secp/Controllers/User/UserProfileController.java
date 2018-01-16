package com.visucius.secp.Controllers.User;


import com.google.common.base.Optional;
import com.visucius.secp.DTO.DeviceDTO;
import com.visucius.secp.DTO.RolesOrPermissionDTO;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.visucius.secp.daos.UserDAO;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(UserProfileController.class);

    private final UserDAO userDAO;

    private static final String defaultUserAvatar = "https://user-images.githubusercontent.com/14824913/34922743-f386cabc-f961-11e7-84af-be3f61f41005.png";


    public UserProfileController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public Response modifyProfile(UserDTO userDTO){
        if (userDTO == null){
            throw new WebApplicationException(UserErrorMessage.MODIFY_USER_FAIL_NO_USER_INFO, Response.Status.BAD_REQUEST);
        }
        validateDisplayNameAndAvatarUrl(userDTO);

        User user = getUser(userDTO.getUserID());
        user.setDisplayName(user.getDisplayName());
        user.setAvatarUrl(user.getAvatarUrl());

        userDAO.save(user);

        return Response.status(Response.Status.OK).entity(getUserProfileResponse(user)).build();
    }

    public Response getProfile(long id){
        User user = getUser(id);
        return Response.status(Response.Status.OK).entity(getUserProfileResponse(user)).build();
    }

    private void validateDisplayNameAndAvatarUrl(UserDTO userDTO){
        if (userDTO == null) {
            throw new WebApplicationException(UserErrorMessage.USER_PROFILE_FAILED_NO_USER_PROFILE, Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(userDTO.getDisplayName())) {
            throw new WebApplicationException(UserErrorMessage.DISPLAY_NAME_FAILED_NO_DISPLAY_NAME, Response.Status.BAD_REQUEST);
        }

        if (StringUtils.isBlank(userDTO.getAvatarUrl())) {
            throw new WebApplicationException(UserErrorMessage.AVATAR_URL_FAILED_NO_AVATAR_URL, Response.Status.BAD_REQUEST);
        }
    }

    private User getUser(long userID){
        Optional<User> userOptional = userDAO.find(userID);
        if (!userOptional.isPresent()){
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID,Response.Status.NO_CONTENT);
        }

        return userOptional.get();
    }

    public UserDTO getUserProfileResponse(User user){
        UserDTO userDTO = new UserDTO(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstname());
        userDTO.setLastName(user.getLastname());
        userDTO.setAvatarUrl(getAvatarForUser(user));
        userDTO.setDisplayName(getDisplayNameForUser(user));
        userDTO.setDevices(getDevicesForUser(user));

        Set<RolesOrPermissionDTO> roles = user.getRoles().stream()
            .map(role -> {
                return new RolesOrPermissionDTO(role.getId(), role.getRole());
                }).collect(Collectors.toSet());

        Permission userPermission = user.getPermission();

        RolesOrPermissionDTO permissionForUser = new RolesOrPermissionDTO();
        if(userPermission != null) {
            permissionForUser.setId(userPermission.getId());
            permissionForUser.setName(userPermission.getLevel());
        }

        userDTO.setPermission(permissionForUser);
        userDTO.setNumOfRoles(user.getRoles().size());
        userDTO.setRoles(roles);

        return userDTO;
    }

    private String getDisplayNameForUser(User user) {
        String displayName = user.getDisplayName();

        if(StringUtils.isEmpty(displayName)) {
            displayName =  user.getUsername();
        }

        return displayName;
    }

    private String getAvatarForUser(User user) {
        String avatarUrl = user.getAvatarUrl();

        if(StringUtils.isEmpty(avatarUrl)) {
            avatarUrl =  defaultUserAvatar;
        }

        return avatarUrl;
    }

    private Set<DeviceDTO> getDevicesForUser(User user) {
        //return devices that have a public key
        return user.getDevices().stream()
            .filter(device -> !StringUtils.isEmpty(device.getPublicKey()))
            .map(device -> {
                return new DeviceDTO(device.getId(), device.getPublicKey());
            }).collect(Collectors.toSet());
    }
}
