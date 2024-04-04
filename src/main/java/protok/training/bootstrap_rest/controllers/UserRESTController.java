package protok.training.bootstrap_rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import protok.training.bootstrap_rest.models.User;
import protok.training.bootstrap_rest.services.RoleService;
import protok.training.bootstrap_rest.services.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/userREST")
@PreAuthorize("hasAuthority('ROLE_USER')")
public class UserRESTController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserRESTController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/user")
    public ResponseEntity<User> showPrincipalData(Principal principal) {

        System.out.println("userREST/user GET");
        User user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok(user);
    }
}
