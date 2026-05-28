package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Конфігураційний клас для налаштування безпеки вебдодатку.
 * Визначає правила доступу до URL, налаштування форми логіну та виходу,
 * а також провайдера автентифікації.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Сервіс для отримання даних про користувача під час автентифікації.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Створює бін для кодування паролів з використанням алгоритму BCrypt.
     *
     * @return об'єкт {@link PasswordEncoder} для хешування паролів
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Налаштовує та створює провайдера автентифікації, який використовує
     * кастомний сервіс деталей користувача та кодувальник паролів.
     *
     * @return об'єкт {@link DaoAuthenticationProvider} для перевірки облікових даних
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Налаштовує ланцюг фільтрів безпеки для HTTP-запитів.
     * Встановлює правила авторизації, налаштовує сторінку входу, обробник успішного входу
     * та процес виходу з системи.
     *
     * @param http об'єкт {@link HttpSecurity} для конфігурації безпеки
     * @return налаштований {@link SecurityFilterChain}
     * @throws Exception якщо виникає помилка під час налаштування безпеки
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/css/**", "/js/**", "/error").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .permitAll()
                ).logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }
}
