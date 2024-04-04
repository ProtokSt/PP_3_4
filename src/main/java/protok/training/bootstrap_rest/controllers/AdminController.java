package protok.training.bootstrap_rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import protok.training.bootstrap_rest.models.User;
import protok.training.bootstrap_rest.services.RoleService;
import protok.training.bootstrap_rest.services.UserService;

import javax.validation.Valid;
import java.security.Principal;

// OBSOLETE - данный контроллер устарел
// Контроллер с предыдущего шага ПП
// Всё ещё работает и использовался как запасной при написании REST
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // отображение всех
    @GetMapping("/showAllUsers")
    public String showAllUsers(Model model, Principal principal) {
        System.out.println("showAllUsers GET");
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "/admin/all-users";
    }

    // добавление нового.
    // Пояснение: Метод обрабатывает логику нажатия кнопки "Добавить нового пользователя" во фронт интерфейсе
    // панели администратора. Непосредственно передаёт вновь созданный объект - экземпляр класса User в фронт
    // интерфейс для создания формы и последующего заполнения полей экземпляра введёнными на фронте данными.
    @GetMapping("/addNewUser")
    public String addNewUser(Model model, Principal principal) {
        System.out.println("addNewUser GET");
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
        model.addAttribute("allRoles", roleService.getAllRoles());

        return "/admin/user-info";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/user-info";
        }
        System.out.println("userService.findByUsername(" + user.getUsername() + ") = " + userService.findByUsername(user.getUsername()));
        if (userService.findByUsername(user.getUsername()) != null) {
            bindingResult.addError(new FieldError("username", "username",
                    String.format("Запись в БД с таким именем \"%s\" уже существует.!", user.getUsername())));
            return "/admin/user-info";
        }
        userService.addUser(user);
        return "redirect:/admin/showAllUsers";
    }

    ////// редактирование существующего
    @PostMapping("/updateUser/{id}")
    public String updateUser2(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) {
        System.out.println("updateUser2 POST");

        if (bindingResult.hasErrors()) {
            System.out.println("updateUser2 POST bindingResult.hasErrors()");
            return "redirect:/admin/showAllUsers";
        }
        System.out.println("userService.findByUsername(" + user.getUsername() + ") = " + userService.findByUsername(user.getUsername()));
        if (userService.findByUsername(user.getUsername()) != null) {
            System.out.println("updateUser2 POST Запись в БД с таким именем" + user.getUsername() + " уже существует");
            bindingResult.addError(new FieldError("username", "username",
                    String.format("Запись в БД с таким именем \"%s\" уже существует.!", user.getUsername())));
            return "redirect:/admin/showAllUsers";
        }

        userService.updateUser(user);
        return "redirect:/admin/showAllUsers";
    }

    ////// удаление
    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@ModelAttribute("id") Long id) {
        System.out.println("deleteUser GET");
        userService.removeUser(id);

        return "redirect:/admin/showAllUsers";
    }

    @PostMapping("/deleteUser/{id}")
    public String deleteUser2(@ModelAttribute("id") Long id) {
        System.out.println("deleteUser2 POST");
        userService.removeUser(id);

        return "redirect:/admin/showAllUsers";
    }

}
