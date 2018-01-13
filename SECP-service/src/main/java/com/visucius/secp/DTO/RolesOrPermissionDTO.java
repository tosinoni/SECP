package com.visucius.secp.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RolesOrPermissionDTO {
    @JsonProperty
    private long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String color;

    public RolesOrPermissionDTO() {

    }
    public RolesOrPermissionDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public RolesOrPermissionDTO(long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolesOrPermissionDTO)) return false;
        RolesOrPermissionDTO rp = (RolesOrPermissionDTO) o;
        return id == rp.id && name.equals(rp.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
