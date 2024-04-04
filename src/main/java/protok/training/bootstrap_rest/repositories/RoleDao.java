package protok.training.bootstrap_rest.repositories;

import protok.training.bootstrap_rest.models.Role;

import java.util.List;

public interface RoleDao {
    List<Role> getAllRoles();

    Role getRoleById(Long id);

    Role getRole(String name);

    void addRole(Role role);

    void removeRole(Long id);

    void updateRole(Role role);
}
