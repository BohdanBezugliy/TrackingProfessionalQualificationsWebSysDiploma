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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/certificates")
    public String certificates(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        List<UpskillEventEntity> events = upskillEventRepository.findAllByLectureEntity_LectureId(id);
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("events", events);
        model.addAttribute("allDisciplines", disciplineRepository.findAll());
        return "lecturerCertificates";
    }

    @PostMapping("/certificates/upload")
    @org.springframework.transaction.annotation.Transactional
    public String uploadCertificate(@PathVariable Long id, 
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam(value = "disciplineIds", required = false) List<Long> disciplineIds,
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
        
        if (disciplineIds != null && !disciplineIds.isEmpty()) {
            List<DisciplineEntity> selectedDisciplines = disciplineRepository.findAllById(disciplineIds);
            event.setDisciplines(selectedDisciplines);
        }

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

    @GetMapping("/certificates/download/{eventId}")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable Long id, @PathVariable Long eventId) {
        UpskillEventEntity event = upskillEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));
        
        DocumentEntity doc = event.getDocumentEntity();
        if (doc == null || doc.getFileData() == null) {
            throw new RuntimeException("File data not found");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(doc.getContentType()))
                .body(doc.getFileData());
    }
}
