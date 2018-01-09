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
import java.util.List;
import java.util.Set;

public class UserProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(UserProfileController.class);

    private final UserDAO userDAO;

    public UserProfileController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public Response getProfile(String userID){
        if(StringUtils.isBlank(userID) || !StringUtils.isNumeric(userID)) {
            LOG.warn("Empty user id provided.");
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }

        long id = Long.parseLong(userID);
        User user = getUser(id);

        return Response.status(Response.Status.OK).entity(getUserResponse(user)).build();
    }

    public Response setDisplayName(UserDTO userDTO, long userID) {
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

        return Response.status(Response.Status.OK).entity(newuser.getDisplayName()).build();
    }

    public Response getDisplayName(long id) {
        Optional<User> user = userDAO.find(id);

        if(!user.isPresent()){
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }

        User newuser = new User();
        newuser.setDisplayName(userDAO.find(id).get().getDisplayName());
        newuser.setId(userDAO.find(id).get().getId());
        return Response.status(Response.Status.OK).entity(getUserResponse(newuser)).build();

    }

    public Response getAvatarURL(long id) {
        Optional<User> user = userDAO.find(id);

        if(!user.isPresent()){
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID, Response.Status.NO_CONTENT);
        }

        User newuser = new User();
        newuser.setAvatar_url(userDAO.find(id).get().getAvatar_url());
        newuser.setId(userDAO.find(id).get().getId());
        return Response.status(Response.Status.OK).entity(getUserResponse(newuser)).build();
    }

    public Response setAvatarURL(UserDTO userDTO, long userID) {
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

        return Response.status(Response.Status.OK).entity(newuser.getAvatar_url()).build();
    }


    private void validateDisplayName(UserDTO userDTO) {
        if (userDTO == null) {
            throw new WebApplicationException(UserErrorMessage.DISPLAY_NAME_FAILED_NO_DISPLAY_NAME, Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(userDTO.getDisplayName())) {
            throw new WebApplicationException(UserErrorMessage.DISPLAY_NAME_FAILED_NO_DISPLAY_NAME, Response.Status.BAD_REQUEST);
        }
    }

    private void validateAvatarURL(UserDTO userDTO) {
        if (userDTO == null) {
            throw new WebApplicationException(UserErrorMessage.AVATAR_URL_FAILED_NO_AVATAR_URL, Response.Status.BAD_REQUEST);
        }
        if (StringUtils.isBlank(userDTO.getAvatar_url())) {
            throw new WebApplicationException(UserErrorMessage.AVATAR_URL_FAILED_NO_AVATAR_URL, Response.Status.BAD_REQUEST);
        }
    }

    private User getUser(long userID){
        Optional<User> userOptional = userDAO.find(userID);
        if (!userOptional.isPresent()){
            throw new WebApplicationException(UserErrorMessage.USER_ID_INVALID,Response.Status.BAD_REQUEST);
        }

        return userOptional.get();
    }

    private UserDTO getUserResponse(User user){
        UserDTO userDTO = new UserDTO(user.getId());
        userDTO.setAvatar_url(user.getAvatar_url());
        userDTO.setDisplayName(user.getDisplayName());
        userDTO.setUserID(user.getId());

        return userDTO;
    }
}
