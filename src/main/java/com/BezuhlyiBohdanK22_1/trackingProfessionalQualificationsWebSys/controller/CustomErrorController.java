package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Контролер для обробки помилок у веб-додатку.
 * Реалізує інтерфейс {@link ErrorController} для перехоплення та налаштування відображення сторінок помилок.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Обробляє всі помилки, що виникають під час виконання запитів.
     * Визначає HTTP-статус та формує відповідне повідомлення про помилку для відображення користувачу.
     *
     * @param request об'єкт {@link HttpServletRequest}, що містить інформацію про помилку
     * @param model   об'єкт {@link Model} для передачі даних (код статусу та повідомлення) у представлення
     * @return назва HTML-шаблону сторінки помилки ("error")
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String errorMessage = "Виникла непередбачувана помилка.";
        
        if (status != null) {
            statusCode = Integer.parseInt(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                errorMessage = "Сторінку, яку ви шукаєте, не знайдено.";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                errorMessage = "У вас немає доступу до цієї сторінки.";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                errorMessage = "Ви не авторизовані.";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                errorMessage = "Внутрішня помилка сервера. Ми вже працюємо над цим.";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                errorMessage = "Некоректний запит.";
            }
        }
        
        // Можна також дістати текст помилки з атрибуту
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if (message != null && !message.toString().isEmpty()) {
            errorMessage = message.toString();
        }
        
        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage", errorMessage);
        
        return "error";
    }
}
