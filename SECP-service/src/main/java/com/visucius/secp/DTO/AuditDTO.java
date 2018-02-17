package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Set;

public class AuditDTO {
    @JsonProperty
    private UserDTO fromUser;

    @JsonProperty
    private Set<UserDTO> toUsers;

    @JsonProperty
    private Date fromDate;

    @JsonProperty
    private Date toDate;

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
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}
