package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.controller;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DisciplineDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DisciplineEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper.DisciplineMapper;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.stream.Collectors;

/**
 * Контролер для глобального управління дисциплінами.
 * Доступний для адміністраторів і дозволяє переглядати, створювати, оновлювати та видаляти дисципліни.
 */
@Controller
@RequestMapping("/admin/disciplines")
@RequiredArgsConstructor
public class GlobalDisciplineController {

    private final DisciplineRepository disciplineRepository;
    private final DisciplineMapper disciplineMapper;

    /**
     * Відображає сторінку зі списком усіх дисциплін.
     *
     * @param model об'єкт {@link Model} для передачі списку дисциплін у представлення
     * @return назва HTML-шаблону сторінки дисциплін ("globalDisciplines")
     */
    @GetMapping
    @Transactional(readOnly = true)
    public String disciplines(Model model) {
        model.addAttribute("disciplines", disciplineRepository.findAll().stream().map(disciplineMapper::toDto).collect(Collectors.toList()));
        return "globalDisciplines";
    }

    /**
     * Створює нову дисципліну на основі переданих даних.
     *
     * @param dto                об'єкт {@link DisciplineDto} з даними нової дисципліни
     * @param redirectAttributes об'єкт для передачі flash-повідомлень (успіх або помилка)
     * @return редирект на сторінку списку дисциплін
     */
    @PostMapping("/create")
    public String createDiscipline(@ModelAttribute DisciplineDto dto, RedirectAttributes redirectAttributes) {
        try {
            DisciplineEntity discipline = new DisciplineEntity();
            discipline.setDisciplineName(dto.getDisciplineName());
            disciplineRepository.save(discipline);
            redirectAttributes.addFlashAttribute("successMessage", "Дисципліну успішно створено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка збереження! Можливо, дисципліна з такою назвою вже існує.");
        }
        return "redirect:/admin/disciplines";
    }

    /**
     * Оновлює назву існуючої дисципліни.
     *
     * @param id                 ідентифікатор дисципліни для оновлення
     * @param dto                об'єкт {@link DisciplineDto} з новою назвою дисципліни
     * @param redirectAttributes об'єкт для передачі flash-повідомлень (успіх або помилка)
     * @return редирект на сторінку списку дисциплін
     */
    @PostMapping("/update/{id}")
    public String updateDiscipline(@PathVariable Long id, @ModelAttribute DisciplineDto dto, RedirectAttributes redirectAttributes) {
        try {
            DisciplineEntity discipline = disciplineRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Discipline not found"));
            discipline.setDisciplineName(dto.getDisciplineName());
            disciplineRepository.save(discipline);
            redirectAttributes.addFlashAttribute("successMessage", "Дисципліну успішно оновлено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка збереження! Можливо, дисципліна з такою назвою вже існує.");
        }
        return "redirect:/admin/disciplines";
    }

    /**
     * Видаляє дисципліну за її ідентифікатором.
     * Якщо до дисципліни прив'язані документи (сертифікати), видалення неможливе.
     *
     * @param id                 ідентифікатор дисципліни для видалення
     * @param redirectAttributes об'єкт для передачі flash-повідомлень (успіх або помилка)
     * @return редирект на сторінку списку дисциплін
     */
    @PostMapping("/delete/{id}")
    @Transactional
    public String deleteDiscipline(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        DisciplineEntity discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discipline not found"));
        if (discipline.getUpskillEvents() != null && !discipline.getUpskillEvents().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Не неможливо видалити дисципліну, оскільки до неї прив'язані документи!");
            return "redirect:/admin/disciplines";
        }
        disciplineRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Дисципліну успішно видалено!");
        return "redirect:/admin/disciplines";
    }
}
