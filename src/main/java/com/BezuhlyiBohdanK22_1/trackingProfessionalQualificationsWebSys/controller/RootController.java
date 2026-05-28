package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контролер для обробки кореневого шляху (/).
 * Виконує перенаправлення авторизованих користувачів на відповідні панелі
 * керування залежно від їхньої ролі (адміністратор або користувач).
 */
@Controller
public class RootController {

    /**
     * Обробляє GET-запит на кореневий URL (/).
     * Перевіряє права доступу поточного користувача. Якщо це адміністратор, перенаправляє
     * на "/admin/dashboard", якщо викладач - на "/user/dashboard". Незареєстрованих
     * користувачів перенаправляє на сторінку входу ("/login").
     *
     * @param authentication об'єкт {@link Authentication} з інформацією про поточного користувача та його ролі
     * @return редирект на відповідну сторінку в залежності від ролі
     */
    @GetMapping("/")
    public String rootRedirect(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return "redirect:/admin/dashboard";
                }
                if (authority.getAuthority().equals("ROLE_USER")) {
                    return "redirect:/user/dashboard";
                }
            }
        }
        return "redirect:/login";
    }
}
