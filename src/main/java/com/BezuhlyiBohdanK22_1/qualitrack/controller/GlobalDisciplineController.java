package com.BezuhlyiBohdanK22_1.qualitrack.controller;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.DisciplineEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DisciplineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/disciplines")
@RequiredArgsConstructor
public class GlobalDisciplineController {

    private final DisciplineRepository disciplineRepository;

    @GetMapping
    public String disciplines(Model model) {
        model.addAttribute("disciplines", disciplineRepository.findAll());
        return "globalDisciplines";
    }

    @PostMapping("/create")
    public String createDiscipline(@RequestParam String disciplineName, RedirectAttributes redirectAttributes) {
        DisciplineEntity discipline = new DisciplineEntity();
        discipline.setDisciplineName(disciplineName);
        disciplineRepository.save(discipline);
        redirectAttributes.addFlashAttribute("successMessage", "Дисципліну успішно створено!");
        return "redirect:/admin/disciplines";
    }

    @PostMapping("/update/{id}")
    public String updateDiscipline(@PathVariable Long id, @RequestParam String disciplineName, RedirectAttributes redirectAttributes) {
        DisciplineEntity discipline = disciplineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Discipline not found"));
        discipline.setDisciplineName(disciplineName);
        disciplineRepository.save(discipline);
        redirectAttributes.addFlashAttribute("successMessage", "Дисципліну успішно оновлено!");
        return "redirect:/admin/disciplines";
    }

    @PostMapping("/delete/{id}")
    public String deleteDiscipline(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        disciplineRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Дисципліну успішно видалено!");
        return "redirect:/admin/disciplines";
    }
}
