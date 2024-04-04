package protok.training.bootstrap_rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import protok.training.bootstrap_rest.models.User;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
// Паттерн Spring Security - обёртка над сущностью
// Использование внутреннего интерфейса для стандартизации методов получения данных из сущности
// второе главное назначение - возвращать роли=доступные действия, Authorities
public class UserDetailsImpl implements UserDetails {
    private final User user;

    @Autowired
    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(new GrantedAuthorityImpl(role).getAuthority())).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Получение данных пользователя в виде полного объекта
    public User getUser() {
        return this.user;
    }
}
