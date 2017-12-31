package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GroupDTO {
    @JsonProperty
    private long groupID;

    @JsonProperty
    private Set<UserDTO> users = new HashSet<>();

    public GroupDTO(long groupID) {
        this.groupID = groupID;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public void addUser(UserDTO user) {
        this.users.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupDTO)) return false;
        GroupDTO g = (GroupDTO) o;

        return groupID == g.groupID && users.equals(g.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.groupID, this.users);
    }
}
