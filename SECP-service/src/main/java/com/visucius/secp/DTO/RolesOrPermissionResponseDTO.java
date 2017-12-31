package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RolesOrPermissionResponseDTO {
    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    public  RolesOrPermissionResponseDTO() {

    }
    public RolesOrPermissionResponseDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
