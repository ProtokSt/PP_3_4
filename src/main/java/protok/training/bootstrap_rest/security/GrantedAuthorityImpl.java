package protok.training.bootstrap_rest.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import protok.training.bootstrap_rest.models.Role;

@Service
public class GrantedAuthorityImpl implements GrantedAuthority {
    private final Role role;

    public GrantedAuthorityImpl(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName();
    }
}
