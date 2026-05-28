package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.controller;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DisciplineDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LecturerDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.UpskillEventDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.*;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper.DepartmentMapper;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper.DisciplineMapper;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper.LecturerMapper;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper.UpskillEventMapper;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.DepartmentRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.DisciplineRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.DocumentRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.UpskillEventRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service.ILectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.stream.Collectors;

/**
 * Контролер для управління профілями та сертифікатами викладачів адміністратором.
 * Надає адміністратору можливості, аналогічні особистому кабінету викладача,
 * зокрема редагування профілю, зміну пароля, видалення викладача та управління його документами.
 */
@Controller
@RequestMapping("/admin/lecturer/{id}")
@RequiredArgsConstructor
public class LecturerManagementController {

    private final ILectureService lectureService;
    private final DepartmentRepository departmentRepository;
    private final DisciplineRepository disciplineRepository;
    private final UpskillEventRepository upskillEventRepository;
    private final DocumentRepository documentRepository;
    
    private final LecturerMapper lecturerMapper;
    private final UpskillEventMapper upskillEventMapper;
    private final DisciplineMapper disciplineMapper;
    private final DepartmentMapper departmentMapper;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "application/pdf",
            "application/msword", // doc
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // docx
            "image/jpeg",
            "image/png"
    );

    /**
     * Відображає сторінку профілю конкретного викладача для адміністратора.
     *
     * @param id    ідентифікатор викладача
     * @param model об'єкт {@link Model} для передачі даних викладача у представлення
     * @return назва HTML-шаблону профілю викладача ("lecturerProfile")
     */
    @GetMapping("/profile")
    @Transactional(readOnly = true)
    public String profile(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        model.addAttribute("lecturer", lecturerMapper.toDto(lecturer));
        return "lecturerProfile";
    }

    /**
     * Відображає сторінку редагування профілю викладача адміністратором.
     *
     * @param id    ідентифікатор викладача
     * @param model об'єкт {@link Model} для передачі даних викладача та списку кафедр
     * @return назва HTML-шаблону сторінки редагування ("editLecturerProfile")
     */
    @GetMapping("/profile/edit")
    @Transactional(readOnly = true)
    public String editProfile(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        model.addAttribute("lecturer", lecturerMapper.toDto(lecturer));
        model.addAttribute("departments", departmentRepository.findAll().stream().map(departmentMapper::toDto).collect(Collectors.toList()));
        return "editLecturerProfile";
    }

    /**
     * Оновлює основну інформацію в профілі викладача.
     *
     * @param id                 ідентифікатор викладача
     * @param dto                об'єкт {@link LecturerDto} з оновленими даними
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку профілю викладача
     */
    @PostMapping("/profile/update")
    public String updateProfile(@PathVariable Long id, @ModelAttribute LecturerDto dto, RedirectAttributes redirectAttributes) {
        try {
            lectureService.updateProfile(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Профіль успішно оновлено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/lecturer/" + id + "/profile/edit";
        }
        return "redirect:/admin/lecturer/" + id + "/profile";
    }

    /**
     * Оновлює пароль викладача (виконується адміністратором).
     *
     * @param id                 ідентифікатор викладача
     * @param newPassword        новий пароль
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку профілю викладача
     */
    @PostMapping("/profile/password")
    public String updatePassword(@PathVariable Long id, @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        lectureService.updatePassword(id, newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Пароль успішно змінено!");
        return "redirect:/admin/lecturer/" + id + "/profile";
    }

    /**
     * Видаляє профіль викладача з системи.
     *
     * @param id                 ідентифікатор викладача для видалення
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на головну панель адміністратора (дашборд)
     */
    @PostMapping("/delete")
    public String deleteLecturer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        lectureService.deleteLecturerById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Викладача успішно видалено!");
        return "redirect:/admin/dashboard";
    }

    /**
     * Допоміжний клас для зберігання підсумкових даних щодо підвищення кваліфікації за рік.
     */
    public static class YearSummary {
        private List<UpskillEventDto> events = new ArrayList<>();
        private BigDecimal totalEcts = BigDecimal.ZERO;
        private int totalHours = 0;

        public void addEvent(UpskillEventDto event) {
            events.add(event);
            if (event.getEctsCredits() != null) {
                totalEcts = totalEcts.add(event.getEctsCredits());
            }
            if (event.getHours() != null) {
                totalHours += event.getHours();
            }
        }
        public List<UpskillEventDto> getEvents() { return events; }
        public BigDecimal getTotalEcts() { return totalEcts; }
        public int getTotalHours() { return totalHours; }
    }

    /**
     * Відображає сторінку з сертифікатами (документами про підвищення кваліфікації) конкретного викладача.
     * Дані групуються за дисципліною та роком.
     *
     * @param id    ідентифікатор викладача
     * @param model об'єкт {@link Model} для передачі даних у представлення
     * @return назва HTML-шаблону сторінки сертифікатів ("lecturerCertificates")
     */
    @GetMapping("/certificates")
    @Transactional(readOnly = true)
    public String certificates(@PathVariable Long id, Model model) {
        LectureEntity lecturer = lectureService.findByLectureId(id);
        List<UpskillEventEntity> events = upskillEventRepository.findAllByLectureEntity_LectureId(id);
        
        Map<DisciplineDto, Map<Integer, YearSummary>> groupedEvents = new TreeMap<>(Comparator.comparing(DisciplineDto::getDisciplineName));
        
        // Force initialization inside transaction to prevent Postgres Large Object exception
        events.forEach(e -> {
            if (e.getDocumentEntity() != null) {
                e.getDocumentEntity().getDocumentType();
            }
            
            UpskillEventDto dto = upskillEventMapper.toDto(e);
            
            int year = e.getDateEnd() != null ? e.getDateEnd().getYear() : 0;
            if (e.getDisciplines() != null && !e.getDisciplines().isEmpty()) {
                for (DisciplineEntity disc : e.getDisciplines()) {
                    groupedEvents
                        .computeIfAbsent(disciplineMapper.toDto(disc), k -> new TreeMap<>(Collections.reverseOrder()))
                        .computeIfAbsent(year, k -> new YearSummary())
                        .addEvent(dto);
                }
            }
        });

        model.addAttribute("lecturer", lecturerMapper.toDto(lecturer));
        model.addAttribute("events", events.stream().map(upskillEventMapper::toDto).collect(Collectors.toList())); // keeping flat list just in case
        model.addAttribute("groupedEvents", groupedEvents);
        model.addAttribute("allDisciplines", disciplineRepository.findAll().stream().map(disciplineMapper::toDto).collect(Collectors.toList()));
        return "lecturerCertificates";
    }

    /**
     * Завантажує новий документ (сертифікат) підвищення кваліфікації для викладача.
     *
     * @param id                 ідентифікатор викладача
     * @param file               завантажений файл (документ)
     * @param documentType       тип документу
     * @param disciplineIds      список ідентифікаторів дисциплін, до яких відноситься сертифікат
     * @param eventDto           об'єкт {@link UpskillEventDto} з даними про захід підвищення кваліфікації
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку сертифікатів викладача
     * @throws IOException у разі помилок при читанні файлу
     */
    @PostMapping("/certificates/upload")
    @Transactional
    public String uploadCertificate(@PathVariable Long id, 
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("documentType") String documentType,
                                    @RequestParam(value = "disciplineIds", required = false) List<Long> disciplineIds,
                                    @ModelAttribute UpskillEventDto eventDto,
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

        UpskillEventEntity event = new UpskillEventEntity();
        event.setDocumentNumber(eventDto.getDocumentNumber());
        event.setInstitutionName(eventDto.getInstitutionName());
        event.setTopic(eventDto.getTopic());
        event.setEctsCredits(eventDto.getEctsCredits());
        event.setHours(eventDto.getHours());
        event.setDateBegin(eventDto.getDateBegin());
        event.setDateEnd(eventDto.getDateEnd());
        event.setDateReceived(eventDto.getDateReceived());

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

    /**
     * Оновлює інформацію про існуючий документ (сертифікат) підвищення кваліфікації.
     *
     * @param id                 ідентифікатор викладача
     * @param eventId            ідентифікатор заходу (документу), що оновлюється
     * @param file               новий файл (якщо потрібно оновити сам документ)
     * @param documentType       новий тип документу
     * @param eventDetails       оновлені деталі заходу
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку сертифікатів викладача
     * @throws IOException у разі помилок при читанні файлу
     */
    @PostMapping("/certificates/update/{eventId}")
    @org.springframework.transaction.annotation.Transactional
    public String updateCertificate(@PathVariable Long id, 
                                    @PathVariable Long eventId,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("documentType") String documentType,
                                    @ModelAttribute UpskillEventDto eventDetails,
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

    /**
     * Видаляє документ (сертифікат) підвищення кваліфікації викладача за його ідентифікатором.
     *
     * @param id                 ідентифікатор викладача
     * @param eventId            ідентифікатор заходу (документу) для видалення
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку сертифікатів викладача
     */
    @PostMapping("/certificates/delete/{eventId}")
    public String deleteCertificate(@PathVariable Long id, @PathVariable Long eventId, RedirectAttributes redirectAttributes) {
        upskillEventRepository.deleteById(eventId);
        redirectAttributes.addFlashAttribute("successMessage", "Документ успішно видалено!");
        return "redirect:/admin/lecturer/" + id + "/certificates";
    }

    /**
     * Завантажує (скачує) файл документу (сертифікату) підвищення кваліфікації.
     *
     * @param id      ідентифікатор викладача
     * @param eventId ідентифікатор заходу, файл якого потрібно завантажити
     * @return об'єкт {@link ResponseEntity} з масивом байтів файлу для його завантаження
     */
    @GetMapping("/certificates/download/{eventId}")
    @Transactional(readOnly = true)
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
