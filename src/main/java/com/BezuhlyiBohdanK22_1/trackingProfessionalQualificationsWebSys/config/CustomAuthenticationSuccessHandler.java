package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

/**
 * Кастомний обробник успішної автентифікації.
 * Відповідає за перенаправлення користувачів на відповідні сторінки (панелі керування)
 * залежно від їхньої ролі (ADMIN або USER) після успішного входу в систему.
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    /**
     * Логер для запису подій, пов'язаних із успішною автентифікацією.
     */
    Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    /**
     * Обробляє подію успішної автентифікації.
     * Визначає роль користувача та виконує перенаправлення на відповідну сторінку панелі керування.
     *
     * @param request        HTTP-запит
     * @param response       HTTP-відповідь для виконання перенаправлення
     * @param authentication об'єкт автентифікації, що містить дані користувача та його ролі
     * @throws IOException якщо виникає помилка вводу/виводу під час перенаправлення
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN") || roles.contains("ADMIN")) {
            response.sendRedirect("/admin/dashboard");
            logger.info("Admin has been successfully logged in");
        } else if (roles.contains("ROLE_USER") || roles.contains("USER")) {
            response.sendRedirect("/user/dashboard");
            logger.info("User has been successfully logged in");
        }
    }
}
