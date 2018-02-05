package com.visucius.secp.Controllers;

import com.visucius.secp.DTO.UserDTO;
import com.visucius.secp.daos.PermissionDAO;
import com.visucius.secp.daos.UserDAO;
import com.visucius.secp.models.LoginRole;
import com.visucius.secp.models.Permission;
import com.visucius.secp.models.User;
import com.visucius.secp.util.PasswordUtil;
import io.dropwizard.hibernate.UnitOfWork;

public class AppSetUpController {
    private final PermissionDAO permissionDAO;
    private final UserDAO userDAO;
    private static final String username = "admin";
    private static final String firstName = "John";
    private static final String lastName = "Doe";
    private static final String email = "admin@secp.com";
    private static final String password = "Admin123";
    private static final String permissionName = "ADMIN";
    private static final String permissionColor = "#00000";

    public AppSetUpController(PermissionDAO permissionDAO, UserDAO userDAO) {
        this.permissionDAO = permissionDAO;
        this.userDAO = userDAO;
    }

    @UnitOfWork
    public void setUp() {
        if(userDAO.findByUserName(username) == null) {
            Permission permission = permissionDAO.save(new Permission(permissionName, permissionColor));

            User user = new User();
            user.setFirstname(firstName);
            user.setLastname(lastName);
            user.setEmail(email);
            user.setUsername(username);
            user.setAvatarUrl(UserDTO.defaultUserAvatar);
            user.setDisplayName(user.getUsername());
            user.setLoginRole(LoginRole.ADMIN);
            user.setPermission(permission);
            try {
                user.setPassword(PasswordUtil.createHash(password));
            } catch (PasswordUtil.CannotPerformOperationException e) {
                e.printStackTrace();
            }

            userDAO.save(user);
        }
    }
}
