package com.BezuhlyiBohdanK22_1.qualitrack.controller;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.LectureDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.DepartmentEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.FacultyEntity;
import com.BezuhlyiBohdanK22_1.qualitrack.service.IDepartmentService;
import com.BezuhlyiBohdanK22_1.qualitrack.service.IFacultyService;
import com.BezuhlyiBohdanK22_1.qualitrack.service.ILectureService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final ILectureService lectureService;
    private final IFacultyService facultyService;
    private final IDepartmentService departmentService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        logger.info("Dashboard Admin");
        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("lecturers", lectureService.findAll());
        return "adminDashboard";
    }

    @PostMapping("/faculty/create")
    public String createFaculty(@ModelAttribute FacultyEntity faculty, RedirectAttributes redirectAttributes) {
        logger.info("Create Faculty: {}", faculty.getFacultyName());
        facultyService.save(faculty);
        redirectAttributes.addFlashAttribute("successMessage", "Факультет успішно створено!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/department/create")
    public String createDepartment(@ModelAttribute DepartmentEntity department, @RequestParam Long facultyId, RedirectAttributes redirectAttributes) {
        logger.info("Create Department: {}", department.getDepartmentName());
        FacultyEntity faculty = facultyService.findById(facultyId);
        department.setFacultyEntity(faculty);
        departmentService.save(department);
        redirectAttributes.addFlashAttribute("successMessage", "Кафедру успішно створено!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/lecturers/create")
    public String createLecturer(@ModelAttribute LectureDto dto, RedirectAttributes redirectAttributes) {
        logger.info("Create Lecturer");
        lectureService.saveLecturer(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Викладача успішно створено!");
        return "redirect:/admin/dashboard";
    }
}
