package com.visucius.secp.Controllers.chat;

import com.visucius.secp.Controllers.GroupController;
import com.visucius.secp.Controllers.User.UserProfileController;
import com.visucius.secp.DTO.GroupDTO;
import com.visucius.secp.DTO.SearchResultDTO;
import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.daos.GroupDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.Group;
import com.visucius.secp.models.User;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChatController {
    private UserDAO userDAO;
    private GroupDAO groupDAO;
    private UserProfileController userProfileController;
    private GroupController groupController;

    public ChatController(UserDAO userDAO, GroupDAO groupDAO,
                          UserProfileController userProfileController, GroupController groupController) {
        this.userDAO = userDAO;
        this.groupDAO = groupDAO;
        this.userProfileController = userProfileController;
        this.groupController = groupController;
    }

    public Response search(User user, String value) {
        if (StringUtils.isEmpty(value)) {
            throw new WebApplicationException(ChatErrorMessage.SEARCH_STRING_INVALID, Response.Status.NO_CONTENT);
        }

        SearchResultDTO searchResultDTO = new SearchResultDTO();

        searchResultDTO.setUsers(getUsersMatchingSearchValue(user, value));
        searchResultDTO.setGroups(getGroupsMatchingSearchValue(value));

        return Response.status(Response.Status.OK).entity(searchResultDTO).build();
    }

    private Set<UserDTO> getUsersMatchingSearchValue(User searchingUser, String value) {
        List<User> users = userDAO.searchForUser(value);
        Set<UserDTO> userDTOS = users.stream()
            .filter(user -> searchingUser.getId() != user.getId() && !user.getDevices().isEmpty())
            .map(user -> {
                return userProfileController.getUserProfileResponse(user); })
            .collect(Collectors.toSet());

        return userDTOS;
    }

    private Set<GroupDTO> getGroupsMatchingSearchValue(String value) {
        List<Group> groups = groupDAO.searchForGroup(value);
        Set<GroupDTO> groupDTOS = groups.stream()
            .map(group -> {
                return groupController.getGroupResponse(group);
        }).collect(Collectors.toSet());

        return groupDTOS;
    }
}
