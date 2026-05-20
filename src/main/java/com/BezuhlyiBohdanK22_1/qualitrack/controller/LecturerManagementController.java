package com.BezuhlyiBohdanK22_1.qualitrack.controller;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.LectureDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.*;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DepartmentRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DisciplineRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DocumentRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.UpskillEventRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.service.ILectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin/lecturer/{id}")
@RequiredArgsConstructor
public class LecturerManagementController {

    private final ILectureService lectureService;
    private final DepartmentRepository departmentRepository;
    private final DisciplineRepository disciplineRepository;
    private final UpskillEventRepository upskillEventRepository;
    private final DocumentRepository documentRepository;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "application/pdf",
            "application/msword", // doc
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // docx
            "image/jpeg",
            "image/png"
    );

    @GetMapping("/profile")
    public String profile(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("departments", departmentRepository.findAll());
        return "lecturerProfile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@PathVariable Long id, @ModelAttribute LectureDto dto, RedirectAttributes redirectAttributes) {
        lectureService.updateProfile(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Профіль успішно оновлено!");
        return "redirect:/admin/lecturer/" + id + "/profile";
    }

    @PostMapping("/profile/password")
    public String updatePassword(@PathVariable Long id, @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        lectureService.updatePassword(id, newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Пароль успішно змінено!");
        return "redirect:/admin/lecturer/" + id + "/profile";
    }

    @PostMapping("/delete")
    public String deleteLecturer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        lectureService.deleteLecturerById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Викладача успішно видалено!");
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/disciplines")
    public String disciplines(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("allDisciplines", disciplineRepository.findAll());
        return "lecturerDisciplines";
    }

    @PostMapping("/disciplines/add")
    public String addDiscipline(@PathVariable Long id, @RequestParam Long disciplineId, RedirectAttributes redirectAttributes) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        DisciplineEntity discipline = disciplineRepository.findById(disciplineId)
                .orElseThrow(() -> new RuntimeException("Discipline not found"));
        
        if (!lecturer.getDisciplines().contains(discipline)) {
            lecturer.getDisciplines().add(discipline);
            lectureService.save(lecturer);
            redirectAttributes.addFlashAttribute("successMessage", "Дисципліну додано!");
        }
        return "redirect:/admin/lecturer/" + id + "/disciplines";
    }

    @PostMapping("/disciplines/remove")
    public String removeDiscipline(@PathVariable Long id, @RequestParam Long disciplineId, RedirectAttributes redirectAttributes) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        lecturer.getDisciplines().removeIf(d -> d.getDisciplineId().equals(disciplineId));
        lectureService.save(lecturer);
        redirectAttributes.addFlashAttribute("successMessage", "Дисципліну вилучено!");
        return "redirect:/admin/lecturer/" + id + "/disciplines";
    }

    @PostMapping("/disciplines/create-global")
    public String createGlobalDiscipline(@PathVariable Long id, @RequestParam String disciplineName, RedirectAttributes redirectAttributes) {
        DisciplineEntity d = new DisciplineEntity();
        d.setDisciplineName(disciplineName);
        disciplineRepository.save(d);
        redirectAttributes.addFlashAttribute("successMessage", "Створено нову дисципліну в довіднику!");
        return "redirect:/admin/lecturer/" + id + "/disciplines";
    }

    @GetMapping("/certificates")
    public String certificates(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        List<UpskillEventEntity> events = upskillEventRepository.findAllByLectureEntity_LectureId(id);
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("events", events);
        return "lecturerCertificates";
    }

    @PostMapping("/certificates/upload")
    public String uploadCertificate(@PathVariable Long id, 
                                    @RequestParam("file") MultipartFile file,
                                    @ModelAttribute UpskillEventEntity event,
                                    RedirectAttributes redirectAttributes) throws IOException {
        
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Файл не обрано!");
            return "redirect:/admin/lecturer/" + id + "/certificates";
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Недопустимий формат файлу. Дозволено: PDF, DOC, DOCX, JPEG, PNG.");
            return "redirect:/admin/lecturer/" + id + "/certificates";
        }

        LectureEntity lecturer = lectureService.findByLectureId(id);

        DocumentEntity doc = new DocumentEntity();
        doc.setFileName(file.getOriginalFilename());
        doc.setContentType(file.getContentType());
        doc.setFileSize(file.getSize());
        doc.setDocumentType("CERTIFICATE");
        doc.setFileData(file.getBytes());
        documentRepository.save(doc);

        event.setLectureEntity(lecturer);
        event.setDocumentEntity(doc);
        upskillEventRepository.save(event);

        redirectAttributes.addFlashAttribute("successMessage", "Сертифікат успішно завантажено!");
        return "redirect:/admin/lecturer/" + id + "/certificates";
    }

    @PostMapping("/certificates/delete/{eventId}")
    public String deleteCertificate(@PathVariable Long id, @PathVariable Long eventId, RedirectAttributes redirectAttributes) {
        upskillEventRepository.deleteById(eventId);
        redirectAttributes.addFlashAttribute("successMessage", "Сертифікат успішно видалено!");
        return "redirect:/admin/lecturer/" + id + "/certificates";
    }
}
