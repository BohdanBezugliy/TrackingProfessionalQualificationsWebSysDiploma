package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.config;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.UserEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Сервіс для отримання деталей користувача, який реалізує інтерфейс {@link UserDetailsService}.
 * Використовується Spring Security для завантаження даних користувача з бази даних
 * за його електронною поштою під час автентифікації.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    /**
     * Репозиторій для доступу до даних користувачів у базі даних.
     */
    private final UserRepository userRepository;

    /**
     * Завантажує дані користувача за його адресою електронної пошти.
     * Перетворює знайдену сутність користувача на об'єкт {@link UserDetails},
     * необхідний для подальшої роботи Spring Security.
     *
     * @param email адреса електронної пошти користувача, яка використовується як ім'я користувача (username)
     * @return об'єкт {@link UserDetails}, що містить інформацію про користувача, його пароль та права доступу
     * @throws UsernameNotFoundException якщо користувача з вказаною електронною поштою не знайдено в базі даних
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new User(user.getEmail(),user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getUserRole().name())));
    }
}
