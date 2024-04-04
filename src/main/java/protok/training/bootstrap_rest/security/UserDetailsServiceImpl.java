package protok.training.bootstrap_rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import protok.training.bootstrap_rest.models.User;
import protok.training.bootstrap_rest.repositories.UserDao;

// отключаю так как свёл в общий сервис
// ругается при создании общего сервиса в WebSecurityConfig.class
// значит возвращаю отдельный
@Service
@Transactional(readOnly = true) // Создаст прокси класс для выполнения внутренних вызовов в одной транзакции.
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Such user not found!");

        return new UserDetailsImpl(user);
    }
}
