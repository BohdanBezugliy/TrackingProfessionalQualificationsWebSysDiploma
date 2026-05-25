package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.controller;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DepartmentDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.FacultyDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LectureDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DepartmentEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.FacultyEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper.DepartmentMapper;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper.FacultyMapper;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper.LecturerMapper;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service.IDepartmentService;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service.IFacultyService;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service.ILectureService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final ILectureService lectureService;
    private final IFacultyService facultyService;
    private final IDepartmentService departmentService;
    
    private final FacultyMapper facultyMapper;
    private final DepartmentMapper departmentMapper;
    private final LecturerMapper lecturerMapper;

    @GetMapping("/dashboard")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public String dashboard(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) Long facultyId,
                            @RequestParam(required = false) Long departmentId,
                            Model model) {
        logger.info("Dashboard Admin - Search params: keyword={}, facultyId={}, departmentId={}", keyword, facultyId, departmentId);
        
        model.addAttribute("faculties", facultyService.findAll().stream().map(facultyMapper::toDto).collect(Collectors.toList()));
        model.addAttribute("departments", departmentService.findAll().stream().map(departmentMapper::toDto).collect(Collectors.toList()));
        
        if ((keyword != null && !keyword.trim().isEmpty()) || facultyId != null || departmentId != null) {
            model.addAttribute("lecturers", lectureService.searchLecturers(keyword, facultyId, departmentId).stream().map(lecturerMapper::toDto).collect(Collectors.toList()));
        } else {
            model.addAttribute("lecturers", lectureService.findAll().stream().map(lecturerMapper::toDto).collect(Collectors.toList()));
        }
        
        return "adminDashboard";
    }

    @GetMapping("/structure")
    public String structure(Model model) {
        logger.info("Structure Management Admin");
        model.addAttribute("faculties", facultyService.findAll().stream().map(facultyMapper::toDto).collect(Collectors.toList()));
        model.addAttribute("departments", departmentService.findAll().stream().map(departmentMapper::toDto).collect(Collectors.toList()));
        return "facultiesAndDepartments";
    }

    @PostMapping("/faculty/create")
    public String createFaculty(@ModelAttribute FacultyDto dto, RedirectAttributes redirectAttributes) {
        logger.info("Create Faculty: {}", dto.getFacultyName());
        try {
            FacultyEntity faculty = new FacultyEntity();
            faculty.setFacultyName(dto.getFacultyName());
            facultyService.save(faculty);
            redirectAttributes.addFlashAttribute("successMessage", "Факультет успішно створено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка збереження! Можливо, такий факультет вже існує.");
        }
        return "redirect:/admin/structure";
    }

    @PostMapping("/faculty/update/{id}")
    public String updateFaculty(@PathVariable Long id, @RequestParam String facultyName, RedirectAttributes redirectAttributes) {
        logger.info("Update Faculty: {}", id);
        try {
            FacultyEntity faculty = facultyService.findById(id);
            faculty.setFacultyName(facultyName);
            facultyService.save(faculty);
            redirectAttributes.addFlashAttribute("successMessage", "Факультет успішно оновлено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка збереження! Можливо, такий факультет вже існує.");
        }
        return "redirect:/admin/structure";
    }

    @PostMapping("/faculty/delete/{id}")
    public String deleteFaculty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Delete Faculty: {}", id);
        try {
            facultyService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Факультет успішно видалено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Не неможливо видалити факультет, оскільки до нього прив'язані кафедри або викладачі!");
        }
        return "redirect:/admin/structure";
    }

    @PostMapping("/department/create")
    public String createDepartment(@ModelAttribute DepartmentDto dto, RedirectAttributes redirectAttributes) {
        logger.info("Create Department: {}", dto.getDepartmentName());
        try {
            DepartmentEntity department = new DepartmentEntity();
            department.setDepartmentName(dto.getDepartmentName());
            FacultyEntity faculty = facultyService.findById(dto.getFacultyId());
            department.setFacultyEntity(faculty);
            departmentService.save(department);
            redirectAttributes.addFlashAttribute("successMessage", "Кафедру успішно створено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка збереження! Можливо, така кафедра вже існує.");
        }
        return "redirect:/admin/structure";
    }

    @PostMapping("/department/update/{id}")
    public String updateDepartment(@PathVariable Long id, @RequestParam String departmentName, @RequestParam Long facultyId, RedirectAttributes redirectAttributes) {
        logger.info("Update Department: {}", id);
        try {
            DepartmentEntity department = departmentService.findById(id);
            department.setDepartmentName(departmentName);
            FacultyEntity faculty = facultyService.findById(facultyId);
            department.setFacultyEntity(faculty);
            departmentService.save(department);
            redirectAttributes.addFlashAttribute("successMessage", "Кафедру успішно оновлено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Помилка збереження! Можливо, така кафедра вже існує.");
        }
        return "redirect:/admin/structure";
    }

    @PostMapping("/department/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Delete Department: {}", id);
        try {
            departmentService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Кафедру успішно видалено!");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Не неможливо видалити кафедру, оскільки до неї прив'язані викладачі!");
        }
        return "redirect:/admin/structure";
    }

    @PostMapping("/lecturers/create")
    public String createLecturer(@ModelAttribute LectureDto dto, RedirectAttributes redirectAttributes) {
        logger.info("Create Lecturer");
        lectureService.saveLecturer(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Викладача успішно створено!");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/lecturers/delete/{id}")
    public String deleteLecturer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Delete Lecturer with ID: {}", id);
        lectureService.deleteLecturerById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Викладача успішно видалено!");
        return "redirect:/admin/dashboard";
    }
}
