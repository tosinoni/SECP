package com.visucius.secp.Controllers;

public interface GroupErrorMessages {

    String GROUP_PERMISSIONS_REQUIRED = "Group must have at least one permission level";
    String GROUP_NAME_INVALID = "Group name is not valid.";
    String PERMISSION_ID_INVALID = "Permission ID %d is not valid.";
    String ROLE_ID_INVALID = "Roles ID %d is not valid.";
    String GROUP_DOES_NOT_EXIST = "Group does not exist";
    String GROUP_MODIFY_OVERLAP_ROLES = "Overlap in the add and remove role fields";
    String GROUP_MODIFY_OVERLAP_PERMISSIONS = "Overlap in the add and remove permission fields";

}
