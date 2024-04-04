package protok.training.bootstrap_rest.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //Убираем наш AuthenticationProvider так как будем использовать встроенный аппарат Security
    // сервис собран в один класс
    // разобрал обратно
    private final UserDetailsService userDetailsService;
    private final SuccessUserHandler successUserHandler;


    public WebSecurityConfig(UserDetailsService userDetailsService, SuccessUserHandler successUserHandler) {
        this.userDetailsService = userDetailsService;
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Приём http запроса
        // Конфигурация Security
        // Конфигурация авторизации
        http.csrf().disable() // отключаем защиту от межсайтовой подделки запросов
                .authorizeRequests()
                .antMatchers("/login", "/baseRolesInitiation", "/error").permitAll() // общий доступ
                .anyRequest().authenticated()

                .and().formLogin().loginPage("/login")
                .loginProcessingUrl("/process_login")
                .successHandler(successUserHandler)
//                .defaultSuccessUrl("/user", true) // пока указан дефолт, кастомный хендлер не включается?
                .failureUrl("/login?error")

                // стирание сессии и куки - разлогинивание. Встроенный функционал Security
                .and().logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login");
    }


}