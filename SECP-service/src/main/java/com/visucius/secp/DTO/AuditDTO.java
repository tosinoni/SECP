package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AuditDTO {
    @JsonProperty
    private UserDTO fromUser;

    @JsonProperty
    private Set<UserDTO> toUsers = new HashSet<>();

    @JsonProperty
    private Date fromDate = new Date();

    @JsonProperty
    private Date toDate = new Date();

    @JsonProperty
    private Set<GroupDTO> groups = new HashSet<>();

    public AuditDTO() {

    }

    public UserDTO getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserDTO fromUser) {
        this.fromUser = fromUser;
    }

    public Set<UserDTO> getToUsers() {
        return toUsers;
    }

    public void setToUsers(Set<UserDTO> toUsers) {
        this.toUsers = toUsers;
    }

    public Date getFromDate() {
        return new Date(fromDate.getTime());
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = new Date(fromDate.getTime());
    }

    public Date getToDate() {
        return new Date(toDate.getTime());
    }

    public void setToDate(Date toDate) {
        this.toDate = new Date(toDate.getTime());
    }

    public Set<GroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupDTO> groups) {
        this.groups = groups;
    }
}
