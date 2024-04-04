package protok.training.bootstrap_rest.repositories;

import org.springframework.stereotype.Repository;
import protok.training.bootstrap_rest.models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> userTypedQuery = (entityManager.createQuery("select u from User u " +
                "join fetch u.roles where u.username = :username",User.class));
        userTypedQuery.setParameter("username",username);
        return userTypedQuery.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void removeUser(Long id) {
        entityManager.remove(getUserById(id));
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }
}
