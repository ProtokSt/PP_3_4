package protok.training.bootstrap_rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import protok.training.bootstrap_rest.models.Role;
import protok.training.bootstrap_rest.models.User;
import protok.training.bootstrap_rest.repositories.UserDao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional // Создаст прокси класс для выполнения внутренних вызовов в одной транзакции.
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        return user;
    }

    @Override
    public void addUser(User user) {
        // назначение роли поумолчанию, если пользователь объявлен без ролей
        if (user.getRoles() == null) {
            Set<Role> userRole = new HashSet<>();
            userRole.add(roleService.getRoleById(1L));
            user.setRoles(userRole);
        } else {
            user.setRoles(user.getRoles().stream()
                    .map(role -> roleService.getRole(role.getName()))
                    .collect(Collectors.toSet()));
        }

        // шифрование, пароля, иначе сохранится простым, а пытаться войти будет через шифрованый
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.addUser(user);
    }

    @Override
    public void removeUser(Long id) {
        userDao.removeUser(id);
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getRole(role.getName()))
                .collect(Collectors.toSet()));
        userDao.updateUser(user);
    }

}
