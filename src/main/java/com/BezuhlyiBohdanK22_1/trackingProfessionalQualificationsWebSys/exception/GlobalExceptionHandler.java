package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Глобальний обробник винятків, який перехоплює помилки на рівні контролерів.
 * Використовує механізм {@link ControllerAdvice} для централізованої обробки винятків
 * у всьому вебдодатку.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обробляє виняток {@link UserAlreadyExistsException}, який виникає при спробі
     * зареєструвати користувача з email, що вже існує в системі.
     * Зберігає повідомлення про помилку у flash-атрибутах та перенаправляє
     * користувача на попередню сторінку.
     *
     * @param ex                 виняток, що містить повідомлення про помилку
     * @param request            об'єкт HTTP-запиту для отримання заголовка Referer
     * @param redirectAttributes об'єкт для передачі атрибутів при перенаправленні (наприклад, повідомлення про помилку)
     * @return рядок з URL-адресою для перенаправлення
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        
        // Повертаємо користувача на ту ж сторінку, де сталася помилка
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        
        // Запасний варіант
        return "redirect:/admin/dashboard";
    }
}
