package com.visucius.secp.Controllers.chat;

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

    public ChatController(UserDAO userDAO, GroupDAO groupDAO) {
        this.userDAO = userDAO;
        this.groupDAO = groupDAO;
    }

    public Response search(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new WebApplicationException(ChatErrorMessage.SEARCH_STRING_INVALID, Response.Status.NO_CONTENT);
        }

        SearchResultDTO searchResultDTO = new SearchResultDTO();

        searchResultDTO.setUsers(getUsersMatchingSearchValue(value));
        searchResultDTO.setGroups(getGroupsMatchingSearchValue(value));

        return Response.status(Response.Status.OK).entity(searchResultDTO).build();
    }

    private Set<UserDTO> getUsersMatchingSearchValue(String value) {
        List<User> users = userDAO.searchForUser(value);
        Set<UserDTO> userDTOS = users.stream().map(user -> {
            UserDTO userDTO = new UserDTO(user.getId());
            userDTO.setUsername(user.getUsername());
            return userDTO;
        }).collect(Collectors.toSet());

        return userDTOS;
    }

    private Set<GroupDTO> getGroupsMatchingSearchValue(String value) {
        List<Group> groups = groupDAO.searchForGroup(value);
        Set<GroupDTO> groupDTOS = groups.stream().map(group -> {
            GroupDTO groupDTO = new GroupDTO(group.getId());
            groupDTO.setName(group.getName());
            return groupDTO;
        }).collect(Collectors.toSet());

        return groupDTOS;
    }
}
