package protok.training.bootstrap_rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import protok.training.bootstrap_rest.models.User;
import protok.training.bootstrap_rest.services.RoleService;
import protok.training.bootstrap_rest.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

// OBSOLETE - данный контроллер устарел
// Контроллер с предыдущего шага ПП
// Всё ещё работает и использовался как запасной при написании REST
@Controller
@PreAuthorize("hasAuthority('ROLE_USER')")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/user")
    public String showPrincipalData(Model model, Principal principal) {
        System.out.println("user GET");
        List<User> accessUsers = new ArrayList<>();
        accessUsers.add(userService.findByUsername(principal.getName()));
        model.addAttribute("allUsers", accessUsers);
        return "/user";
    }
}
