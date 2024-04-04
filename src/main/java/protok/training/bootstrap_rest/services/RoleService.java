package protok.training.bootstrap_rest.services;

import protok.training.bootstrap_rest.models.Role;

import java.util.List;

public interface RoleService {
    // Это бизнес логика? Тогда мне не нужно ничего кроме создания исходных ролей,
    // ещё добавлю получение роли по id,
    // но если выносить генерацию базовых ролей в отдельный утиль класс,
    // то тогда логично сделать весь сервис в доступности.
    // и наверно это правильно

    List<Role> getAllRoles();

    Role getRoleById(Long id);

    Role getRole(String name);

    void addRole(Role role);

    void removeRole(Long id);

    void updateRole(Role role);

}
