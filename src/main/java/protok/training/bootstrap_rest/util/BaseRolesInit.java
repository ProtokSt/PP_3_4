package protok.training.bootstrap_rest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import protok.training.bootstrap_rest.models.User;
import protok.training.bootstrap_rest.services.RoleService;
import protok.training.bootstrap_rest.services.UserService;
import protok.training.bootstrap_rest.models.Role;

import java.util.HashSet;
import java.util.Set;

@Component
public class BaseRolesInit {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public BaseRolesInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public Boolean doInit() {
        try {
            if (roleService.getRoleById(1L) == null)
                roleService.addRole(new Role("ROLE_USER"));
            if (roleService.getRoleById(2L) == null)
                roleService.addRole(new Role("ROLE_ADMIN"));

            Set<Role> adminRoles = new HashSet<>();
            Set<Role> userRoles = new HashSet<>();
            adminRoles.add(roleService.getRoleById(1L));
            adminRoles.add(roleService.getRoleById(2L));
            userRoles.add(roleService.getRoleById(1L));

            User admin = new User("admin", "admin", "PST", 1000000, adminRoles);
            User user = new User("user", "user", "PST", 500000, userRoles);
            if (userService.findByUsername("admin") == null)
                userService.addUser(admin);

            if (userService.findByUsername("user") == null)
                userService.addUser(user);

        } catch (Exception e) {
            System.err.println("BaseRolesInit/doInit error: " + e);
            return false;
        }
        return true;
    }


}
