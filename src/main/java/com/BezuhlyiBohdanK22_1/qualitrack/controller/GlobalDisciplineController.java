package com.BezuhlyiBohdanK22_1.qualitrack.controller;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.DisciplineEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
@RequestMapping("/admin/disciplines")
@RequiredArgsConstructor
public class GlobalDisciplineController {

    private final DisciplineRepository disciplineRepository;

    @GetMapping
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public String disciplines(Model model) {
        model.addAttribute("disciplines", disciplineRepository.findAll());
        return "globalDisciplines";
    }

    @PostMapping("/create")
    public String createDiscipline(@RequestParam String disciplineName, RedirectAttributes redirectAttributes) {
        try {
            DisciplineEntity discipline = new DisciplineEntity();
            discipline.setDisciplineName(disciplineName);
            disciplineRepository.save(discipline);
            redirectAttributes.addFlashAttribute("successMessage", "Дисципліну успішно створено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка збереження! Можливо, дисципліна з такою назвою вже існує.");
        }
        return "redirect:/admin/disciplines";
    }

    @PostMapping("/update/{id}")
    public String updateDiscipline(@PathVariable Long id, @RequestParam String disciplineName, RedirectAttributes redirectAttributes) {
        try {
            DisciplineEntity discipline = disciplineRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Discipline not found"));
            discipline.setDisciplineName(disciplineName);
            disciplineRepository.save(discipline);
            redirectAttributes.addFlashAttribute("successMessage", "Дисципліну успішно оновлено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка збереження! Можливо, дисципліна з такою назвою вже існує.");
        }
        return "redirect:/admin/disciplines";
    }

    @PostMapping("/delete/{id}")
    @org.springframework.transaction.annotation.Transactional
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
