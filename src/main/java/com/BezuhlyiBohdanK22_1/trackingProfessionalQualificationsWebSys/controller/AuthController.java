package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контролер для обробки запитів, пов'язаних з автентифікацією користувачів.
 * Відповідає за відображення сторінки входу в систему.
 */
@Controller
public class AuthController {
    /**
     * Обробляє GET-запит для відображення сторінки авторизації.
     *
     * @return назва HTML-шаблону сторінки входу ("login")
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
