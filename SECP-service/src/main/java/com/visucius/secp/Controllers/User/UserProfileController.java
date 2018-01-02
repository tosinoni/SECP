package com.visucius.secp.Controllers.User;


import com.google.common.base.Optional;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.models.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.visucius.secp.daos.UserDAO;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class UserProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(UserProfileController.class);

    private final UserDAO userDAO;

    public UserProfileController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public Response getProfile(String userID){
        long id = Long.parseLong(userID);
        Optional<User> user = userDAO.find(id);

        if(!user.isPresent()) {
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }
        return Response.status(Response.Status.OK).entity(user.get()).build();
    }

    public void setDisplayName(UserDTO userDTO, long userID) {
        validateDisplayName(userDTO);

        String displayname = userDTO.getDisplayName();

        Optional<User> user = userDAO.find(userID);
        if(!user.isPresent()){
            throw new WebApplicationException(UserErrorMessage.DISPLAY_NAME_FAILED_USER_NOT_FOUND, Response.Status.BAD_REQUEST);
        }
        User newuser = user.get();
        newuser.setDisplayName(displayname);
        userDAO.save(newuser);
        LOG.info("Display name set.");
    }

    public Response getDisplayName(long id) {
        Optional<User> user = userDAO.find(id);

        if(!user.isPresent()){
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }
        return Response.status(Response.Status.OK).entity(user.get().getDisplayName()).build();

    }

    public Response getAvatarURL(long id) {
        Optional<User> user = userDAO.find(id);

        if(!user.isPresent()){
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }

        return Response.status(Response.Status.OK).entity(user.get().getAvatar_url()).build();
    }

    public void setAvatarURL(UserDTO userDTO, long userID) {
        validateAvatarURL(userDTO);

        String AvatarURL = userDTO.getAvatar_url();

        Optional<User> user = userDAO.find(userID);
        if(!user.isPresent()){
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }
        User newuser = user.get();
        newuser.setAvatar_url(AvatarURL);
        userDAO.save(newuser);
        LOG.info("Avatar URL set.");
    }


    private void validateDisplayName(UserDTO userDTO) {
        if (userDTO == null) {
            throw new WebApplicationException(UserErrorMessage.DISPLAY_NAME_FAILED, Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(userDTO.getDisplayName())) {
            throw new WebApplicationException(UserErrorMessage.DISPLAY_NAME_FAILED_NO_DISPLAY_NAME, Response.Status.BAD_REQUEST);
        }
    }

    private void validateAvatarURL(UserDTO userDTO) {
        if (userDTO == null) {
            throw new WebApplicationException(UserErrorMessage.AVATAR_URL_FAILED, Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(userDTO.getAvatar_url())) {
            throw new WebApplicationException(UserErrorMessage.AVATAR_URL_FAILED_NO_AVATAR_URL, Response.Status.BAD_REQUEST);
        }
    }


}
