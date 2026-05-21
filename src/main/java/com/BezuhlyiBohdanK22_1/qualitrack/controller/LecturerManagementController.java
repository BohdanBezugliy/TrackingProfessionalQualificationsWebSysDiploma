package com.BezuhlyiBohdanK22_1.qualitrack.controller;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.LectureDto;
import com.BezuhlyiBohdanK22_1.qualitrack.dto.LecturerUpdateDto;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public String profile(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        model.addAttribute("lecturer", lecturer);
        return "lecturerProfile";
    }

    @GetMapping("/profile/edit")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public String editProfile(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        model.addAttribute("lecturer", lecturer);
        model.addAttribute("departments", departmentRepository.findAll());
        return "editLecturerProfile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@PathVariable Long id, @ModelAttribute LecturerUpdateDto dto, RedirectAttributes redirectAttributes) {
        try {
            lectureService.updateProfile(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Профіль успішно оновлено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/lecturer/" + id + "/profile/edit";
        }
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

    public static class YearSummary {
        private List<UpskillEventEntity> events = new ArrayList<>();
        private BigDecimal totalEcts = BigDecimal.ZERO;
        private int totalHours = 0;

        public void addEvent(UpskillEventEntity event) {
            events.add(event);
            if (event.getEctsCredits() != null) {
                totalEcts = totalEcts.add(event.getEctsCredits());
            }
            if (event.getHours() != null) {
                totalHours += event.getHours();
            }
        }
        public List<UpskillEventEntity> getEvents() { return events; }
        public BigDecimal getTotalEcts() { return totalEcts; }
        public int getTotalHours() { return totalHours; }
    }

    @GetMapping("/certificates")
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public String certificates(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        List<UpskillEventEntity> events = upskillEventRepository.findAllByLectureEntity_LectureId(id);
        
        Map<DisciplineEntity, Map<Integer, YearSummary>> groupedEvents = new TreeMap<>(Comparator.comparing(DisciplineEntity::getDisciplineName));
        
        // Force initialization inside transaction to prevent Postgres Large Object exception
        events.forEach(e -> {
            if (e.getDocumentEntity() != null) {
                e.getDocumentEntity().getDocumentType();
            }
            
            int year = e.getDateEnd() != null ? e.getDateEnd().getYear() : 0;
            if (e.getDisciplines() != null && !e.getDisciplines().isEmpty()) {
                for (DisciplineEntity disc : e.getDisciplines()) {
                    groupedEvents
                        .computeIfAbsent(disc, k -> new TreeMap<>(Collections.reverseOrder()))
                        .computeIfAbsent(year, k -> new YearSummary())
                        .addEvent(e);
                }
            }
        });

        model.addAttribute("lecturer", lecturer);
        model.addAttribute("events", events); // keeping flat list just in case
        model.addAttribute("groupedEvents", groupedEvents);
        model.addAttribute("allDisciplines", disciplineRepository.findAll());
        return "lecturerCertificates";
    }

    @PostMapping("/certificates/upload")
    @org.springframework.transaction.annotation.Transactional
    public String uploadCertificate(@PathVariable Long id, 
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("documentType") String documentType,
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
        doc.setDocumentType(documentType);
        doc.setFileData(file.getBytes());
        documentRepository.save(doc);

        event.setLectureEntity(lecturer);
        event.setDocumentEntity(doc);
        
        if (disciplineIds != null && !disciplineIds.isEmpty()) {
            List<DisciplineEntity> selectedDisciplines = disciplineRepository.findAllById(disciplineIds);
            event.setDisciplines(selectedDisciplines);
        }

        upskillEventRepository.save(event);

        redirectAttributes.addFlashAttribute("successMessage", "Документ успішно додано!");
        return "redirect:/admin/lecturer/" + id + "/certificates";
    }

    @PostMapping("/certificates/update/{eventId}")
    @org.springframework.transaction.annotation.Transactional
    public String updateCertificate(@PathVariable Long id, 
                                    @PathVariable Long eventId,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("documentType") String documentType,
                                    @ModelAttribute UpskillEventEntity eventDetails,
                                    RedirectAttributes redirectAttributes) throws IOException {
        
        UpskillEventEntity existingEvent = upskillEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        if (file != null && !file.isEmpty()) {
            if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Недопустимий формат файлу. Дозволено: PDF, DOC, DOCX, JPEG, PNG.");
                return "redirect:/admin/lecturer/" + id + "/certificates";
            }
            DocumentEntity doc = existingEvent.getDocumentEntity();
            doc.setFileName(file.getOriginalFilename());
            doc.setContentType(file.getContentType());
            doc.setFileSize(file.getSize());
            doc.setFileData(file.getBytes());
        }
        
        existingEvent.getDocumentEntity().setDocumentType(documentType);
        existingEvent.setDocumentNumber(eventDetails.getDocumentNumber());
        existingEvent.setInstitutionName(eventDetails.getInstitutionName());
        existingEvent.setTopic(eventDetails.getTopic());
        existingEvent.setEctsCredits(eventDetails.getEctsCredits());
        existingEvent.setHours(eventDetails.getHours());
        existingEvent.setDateBegin(eventDetails.getDateBegin());
        existingEvent.setDateEnd(eventDetails.getDateEnd());
        existingEvent.setDateReceived(eventDetails.getDateReceived());
        
        upskillEventRepository.save(existingEvent);
        
        redirectAttributes.addFlashAttribute("successMessage", "Документ успішно оновлено!");
        return "redirect:/admin/lecturer/" + id + "/certificates";
    }

    @PostMapping("/certificates/delete/{eventId}")
    public String deleteCertificate(@PathVariable Long id, @PathVariable Long eventId, RedirectAttributes redirectAttributes) {
        upskillEventRepository.deleteById(eventId);
        redirectAttributes.addFlashAttribute("successMessage", "Документ успішно видалено!");
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
