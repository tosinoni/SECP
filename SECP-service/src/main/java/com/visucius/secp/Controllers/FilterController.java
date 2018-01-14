package com.visucius.secp.Controllers;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.visucius.secp.DTO.*;
import com.visucius.secp.daos.FilterDAO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.RolesDAO;
import com.visucius.secp.models.Filter;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.Role;
import com.visucius.secp.util.InputValidator;
import com.visucius.secp.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FilterController {

    private static final Logger LOG = LoggerFactory.getLogger(FilterController.class);

    private FilterDAO filterRepository;
    private RolesDAO rolesRepository;
    private PermissionDAO permissionsRepository;

    public FilterController(FilterDAO filterRepository,
                            RolesDAO rolesRepository,
                            PermissionDAO permissionDAO)
    {
        this.filterRepository = filterRepository;
        this.rolesRepository = rolesRepository;
        this.permissionsRepository = permissionDAO;
    }


    public Response deleteFilter(int id)
    {
        Filter filter = getFilter(id);
        filterRepository.delete(filter);
        return Response.status(Response.Status.OK).build();
    }

    public Response createFilter(FilterCreateRequest request)
    {
        Set<Long> permissions = request.permissions.stream().
            map(permission -> permission.getId()).collect(Collectors.toSet());
        Set<Long> roles = request.roles.stream().
            map(role -> role.getId()).collect(Collectors.toSet());
        String error = validateCreateRequest(request.name,permissions,roles);
        if(StringUtils.isNoneEmpty(error))
        {
                throw new WebApplicationException(
                    error,
                    Response.Status.BAD_REQUEST);
        }

        Filter filter = new Filter(request.name);
        return updateOrCreateFilter(filter,permissions,roles);
    }

    private Response updateOrCreateFilter(Filter filter, Set<Long> permissions, Set<Long> roles)
    {
        filter.setPermissions(getPermissions(permissions));
        filter.setRoles(getRoles(roles));
        Filter createdFilter = filterRepository.save(filter);
        return Response.status(Response.Status.CREATED).entity(getFilterResponse(createdFilter)).build();
    }

    private Filter getFilter(long filterID)
    {
        Optional<Filter> filterOptional = filterRepository.find(filterID);
        if (!filterOptional.isPresent()) {
            throw new WebApplicationException(
                FilterErrorMessages.FILTER_DOES_NOT_EXIST,
                Response.Status.BAD_REQUEST);
        }

        return filterOptional.get();
    }

    public Response getAllFilters() {
        List<Filter> filters = filterRepository.findAll();

        if(Util.isCollectionEmpty(filters)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        Set<FilterDTO> response = filters.stream()
            .map(filter -> {
                return getFilterResponse(filter);
            })
            .collect(Collectors.toSet());

        return Response.status(Response.Status.OK).entity(response).build();
    }

    public Response getFilterGivenId(String id) {
        if(StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        long filterID = Long.parseLong(id);
        Filter filter = getFilter(filterID);

        return Response.status(Response.Status.OK).entity(getFilterResponse(filter)).build();
    }


    private String validateCreateRequest(String name,Set<Long> permissions, Set<Long> roles) {

        if (!isFilterNameValid(name)
            || !InputValidator.isNameValid(name)) {
            return FilterErrorMessages.FILTER_NAME_INVALID;
        }

        return validateRolesAndPermissions(permissions,roles);
    }

    private String validateRolesAndPermissions(Set<Long> permissions, Set<Long> roles) {

        String roleError = validateRoles(roles);
        if (StringUtils.isNoneEmpty(roleError))
            return roleError;

        String permissionError = validatePermissions(permissions);
        if (StringUtils.isNoneEmpty(permissionError))
            return permissionError;

        return StringUtils.EMPTY;
    }

    private String validateRoles(Set<Long> roles)
    {
        for (long roleID : roles) {
            if (!isRoleIdValid(roleID)) {
                return String.format(FilterErrorMessages.ROLE_ID_INVALID, roleID);
            }
        }

        return StringUtils.EMPTY;
    }

    private String validatePermissions(Set<Long> permissions)
    {
        for (long permissionID : permissions) {
            if (!isPermissionValid(permissionID)) {
                return String.format(FilterErrorMessages.PERMISSION_ID_INVALID, permissionID);
            }
        }

        return StringUtils.EMPTY;
    }

    private Set<Role> getRoles(Set<Long> rolesIDs)
    {
        Set<Role> roles = new HashSet<>();
        rolesIDs.forEach(id -> roles.add(rolesRepository.find(id).get()));
        return roles;
    }

    private Set<Permission> getPermissions(Set<Long> permissionID)
    {
        Set<Permission> permissions = new HashSet<>();
        permissionID.forEach(id -> permissions.add(permissionsRepository.find(id).get()));
        return permissions;
    }

    private FilterDTO getFilterResponse (Filter filter) {
        FilterDTO filterDTO = new FilterDTO(filter.getId());
        filterDTO.setName(filter.getName());
        filterDTO.setNumOfPermissions(filter.getPermissions().size());
        filterDTO.setNumOfRoles(filter.getRoles().size());

        Set<RolesOrPermissionDTO> roles = filter.getRoles().stream()
            .map(role -> {
                return new RolesOrPermissionDTO(role.getId(), role.getRole());
            }).collect(Collectors.toSet());

        Set<RolesOrPermissionDTO> permissions = filter.getPermissions().stream()
            .map(permission -> {
                return new RolesOrPermissionDTO(permission.getId(), permission.getLevel());
            }).collect(Collectors.toSet());

        filterDTO.setRoles(roles);
        filterDTO.setPermissions(permissions);

        return filterDTO;
    }

    private boolean isPermissionValid(long id)
    {
        return permissionsRepository.find(id).isPresent();
    }

    private boolean isRoleIdValid(long id)
    {
        return rolesRepository.find((int)id).isPresent();
    }

    private boolean isFilterNameValid(String name)
    {
        return filterRepository.findByName(name) == null;
    }
}
