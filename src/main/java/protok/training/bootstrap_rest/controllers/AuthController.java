package protok.training.bootstrap_rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import protok.training.bootstrap_rest.services.UserService;
import protok.training.bootstrap_rest.util.BaseRolesInit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
public class AuthController {
    private final UserService userService;

    private final BaseRolesInit baseRolesInit;

    private static final Logger logger = LogManager.getLogger("AuthController");

    @Autowired
    public AuthController(UserService userService, BaseRolesInit baseRolesInit) {
        this.userService = userService;
        this.baseRolesInit = baseRolesInit;
    }

    @GetMapping("/login")
    public String loginPage() {
        logger.info("login GET");

        // тест кодировщика. онлайн кодировщики дают неверные результаты
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String password = "user";
        String encodedPassword = passwordEncoder.encode(password);
        logger.info("Password is         : " + password);
        logger.info("Encoded Password is : " + encodedPassword);
        boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);
        logger.info("Password : " + password + "   isPasswordMatch    : " + isPasswordMatch);
        logger.info("");

        return "/login";
    }
    // Метод POST со страницы перехватывает Spring Security

    // Инициализация первичных ролей доступа, дублирование исключено
    @RequestMapping("/baseRolesInitiation")
    public String generateUsers() {

        logger.info("Пропись базовых ролей и пользователей: " + baseRolesInit.doInit());
        return "/login";
    }

    // связка httpServletResponse.sendRedirect из SuccessUserHandler
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/userREST")
    public String upUserREST() {
        logger.info("userREST GET");
        return "userREST";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/adminREST")
    public String upAdminREST() {
        logger.info("adminREST GET");
        return "adminREST";
    }

}
