package protok.training.bootstrap_rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import protok.training.bootstrap_rest.models.Role;
import protok.training.bootstrap_rest.repositories.RoleDao;

import java.util.List;

@Service
@Transactional // Создаст прокси класс для выполнения внутренних вызовов в одной транзакции.
public class RoleServiceImpl implements RoleService {
    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleDao.getRoleById(id);
    }

    @Override
    public Role getRole(String name) {
        // безуспешная попытка обыграть jackson databind
        if (name == "1") {
            return roleDao.getRoleById(Long.parseLong(name, 10));
        } else if (name == "2") {
            return roleDao.getRoleById(Long.parseLong(name, 10));
        }
        return roleDao.getRole(name);
    }

    @Override
    public void addRole(Role role) {
        roleDao.addRole(role);
    }

    @Override
    public void removeRole(Long id) {
        roleDao.removeRole(id);
    }

    @Override
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }

}
