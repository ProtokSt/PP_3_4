package protok.training.bootstrap_rest.repositories;

import org.springframework.stereotype.Repository;
import protok.training.bootstrap_rest.models.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> getAllRoles() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }

    @Override
    public Role getRoleById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role getRole(String name) {
        return entityManager.createQuery("select r from Role r where r.name =: userRole", Role.class)
                .setParameter("userRole", name).getSingleResult();
    }

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);
    }

    @Override
    public void removeRole(Long id) {
        entityManager.remove(getRoleById(id));
    }

    @Override
    public void updateRole(Role role) {
        entityManager.merge(role);
    }
}
