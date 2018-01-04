package com.visucius.secp.DTO;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GroupDTOTest {
    private final long groupID = 1;


    @Test
    public void testGroupID() {
        GroupDTO groupDTO = new GroupDTO(groupID);
        groupDTO.setGroupID(groupID);
        assertEquals("group id is not equal",groupID, groupDTO.getGroupID());
    }

    @Test
    public void testUsers() {
        GroupDTO groupDTO = new GroupDTO(groupID);

        UserDTO userDTO = new UserDTO(1);
        Set<UserDTO> users = new HashSet<>();
        users.add(userDTO);

        groupDTO.setUsers(users);
        assertEquals("users are is not equal", users, groupDTO.getUsers());
    }

    @Test
    public void testConstructor() {
        GroupDTO groupDTO = new GroupDTO(groupID);
        assertEquals("groupD is not equal",groupID, groupDTO.getGroupID());
    }
}
