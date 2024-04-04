package protok.training.bootstrap_rest.repositories;

import protok.training.bootstrap_rest.models.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    User getUserById(Long id);

    User findByUsername(String username);

    void addUser(User user);

    void removeUser(Long id);

    void updateUser(User user);
}
