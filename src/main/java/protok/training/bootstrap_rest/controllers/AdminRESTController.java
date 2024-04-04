package protok.training.bootstrap_rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import protok.training.bootstrap_rest.exception_handling.UserIncorrectData;
import protok.training.bootstrap_rest.exception_handling.DBSomeException;
import protok.training.bootstrap_rest.models.User;
import protok.training.bootstrap_rest.services.RoleService;
import protok.training.bootstrap_rest.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/adminREST")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminRESTController {

    private UserService userService;
    private RoleService roleService;

    private static final Logger logger = LogManager.getLogger("AdminRESTController");

    @Autowired
    public AdminRESTController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    // получение всех
    @GetMapping("/users")
    public ResponseEntity<List<User>> showAllUsers() {
        logger.info("adminREST/users GET");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    // получение одного
    @GetMapping("users/{id}")
    public ResponseEntity<User> getOneUser(@PathVariable("id") long id) {
        logger.info("adminREST/users/{id} GET");
        User retUser = userService.getUserById(id);
        if (retUser == null) {
            throw new DBSomeException("Записи в БД с таким id: " + id + " нет");
        }
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    // изменение
    @PutMapping("/users")
    public ResponseEntity<UserIncorrectData> updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        logger.info("adminREST/users PUT");
        // сперва проверим логин на совпадение, потом id
        // и если это дубль чужого логина, то даём пред
        User checkU = userService.findByUsername(user.getUsername());
        logger.info("userService.findByUsername(" + user.getUsername() + ") = " + checkU);
        if (checkU != null) {
            if (checkU.getId() != user.getId()) {
                bindingResult.addError(new FieldError("username", "username",
                        String.format("Запись в БД с таким именем \"%s\" уже существует!", user.getUsername())));
            }
        }

        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new UserIncorrectData(error), HttpStatus.BAD_REQUEST);
        }

        try {
            userService.updateUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DBSomeException u) {
            throw new DBSomeException("Persistent model or any other exception of DB update");
        }
    }

    // удаление
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserIncorrectData> deleteUser(@PathVariable("id") long id) {
        logger.info("adminREST/users/{id} DELETE");
        try {
            userService.removeUser(id);
            return new ResponseEntity<>(new UserIncorrectData("User deleted"), HttpStatus.OK);
        } catch (DBSomeException u) {
            throw new DBSomeException("Persistent model or any other exception of DB update");
        }
    }

    // добавление
    @PostMapping("/users")
    public ResponseEntity<UserIncorrectData> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        logger.info("adminREST/users POST");

        User checkU = userService.findByUsername(user.getUsername());
        logger.info("userService.findByUsername(" + user.getUsername() + ") = " + checkU);
        if (checkU != null) {
            if (checkU.getId() != user.getId()) {
                bindingResult.addError(new FieldError("username", "username",
                        String.format("Запись в БД с таким именем \"%s\" уже существует!", user.getUsername())));
            }
        }

        if (bindingResult.hasErrors()) {
            String error = getErrorsFromBindingResult(bindingResult);
            return new ResponseEntity<>(new UserIncorrectData(error), HttpStatus.BAD_REQUEST);
        }

        try {
            userService.addUser(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (DBSomeException u) {
            throw new DBSomeException("Persistent model or any other exception of DB write");
        }
    }

    private String getErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
    }


}
