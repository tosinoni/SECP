package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class SearchResultDTO {

    @JsonProperty
    private Set<UserDTO> users = new HashSet();

    @JsonProperty
    private Set<GroupDTO> groups = new HashSet<>();

    public SearchResultDTO() {

    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

    public Set<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupDTO> groups) {
        this.groups = groups;
    }
}
