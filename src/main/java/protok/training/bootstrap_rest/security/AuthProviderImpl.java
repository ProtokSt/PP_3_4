package protok.training.bootstrap_rest.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

//Credentials -> AuthenticationProvider -> Principal
//Сессии (Сервер) и Куки (Пользователь)
//Сессия хранит Principal пользователя и возвращает его по ключу Куки
//@Component
//Убираем наш AuthenticationProvider так как будем использовать встроенный аппарат Security
public class AuthProviderImpl implements AuthenticationProvider {
    // Сервис реализует в том числе встроенный UserDetailsService
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthProviderImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        // обёртка для стандартного получения данных сущности реализующая встроенный UserDetails
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String password = authentication.getCredentials().toString();

        // пример универсального метода получения данных средствами встроенного и реализованного интерфейса UserDetails
        if (!password.equals(userDetails.getPassword()))
            throw new BadCredentialsException("Incorrect password!");

        // возвращение Principal
        return new UsernamePasswordAuthenticationToken(userDetails, password,
                Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}