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
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.LectureRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.UpskillEventRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.UserRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service.ILectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Контролер для особистого кабінету викладача.
 * Відповідає за перегляд та редагування профілю, управління паролем та електронною поштою,
 * а також за завантаження та перегляд сертифікатів підвищення кваліфікації.
 */
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class LecturerPersonalController {

    private final ILectureService lectureService;
    private final LectureRepository lectureRepository;
    private final DepartmentRepository departmentRepository;
    private final DisciplineRepository disciplineRepository;
    private final UpskillEventRepository upskillEventRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;

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
     * Отримує сутність поточного викладача на основі даних авторизації.
     *
     * @param principal об'єкт {@link Principal} з інформацією про поточного користувача
     * @return об'єкт {@link LectureEntity} для поточного викладача
     * @throws RuntimeException якщо користувач не авторизований або профіль не знайдено
     */
    private LectureEntity getCurrentLecturer(Principal principal) {
        if (principal == null) throw new RuntimeException("User not authenticated");
        return lectureRepository.findByUserEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Профіль викладача не знайдено для поточного користувача"));
    }

    /**
     * Перенаправляє користувача на сторінку його профілю.
     *
     * @return редирект на "/user/profile"
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/user/profile";
    }

    /**
     * Відображає сторінку особистого профілю викладача.
     *
     * @param principal об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param model     об'єкт {@link Model} для передачі даних викладача у представлення
     * @return назва HTML-шаблону профілю ("userProfile")
     */
    @GetMapping("/profile")
    @Transactional(readOnly = true)
    public String profile(Principal principal, Model model) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        model.addAttribute("lecturer", lecturerMapper.toDto(lecturer));
        return "userProfile";
    }

    /**
     * Відображає сторінку редагування профілю викладача.
     *
     * @param principal об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param model     об'єкт {@link Model} для передачі даних викладача та списку кафедр у представлення
     * @return назва HTML-шаблону сторінки редагування ("userEditProfile")
     */
    @GetMapping("/profile/edit")
    @Transactional(readOnly = true)
    public String editProfile(Principal principal, Model model) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        model.addAttribute("lecturer", lecturerMapper.toDto(lecturer));
        model.addAttribute("departments", departmentRepository.findAll().stream().map(departmentMapper::toDto).collect(Collectors.toList()));
        return "userEditProfile";
    }

    /**
     * Оновлює основну інформацію в профілі викладача.
     *
     * @param principal          об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param dto                об'єкт {@link LecturerDto} з оновленими даними
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку профілю
     */
    @PostMapping("/profile/update")
    public String updateProfile(Principal principal, @ModelAttribute LecturerDto dto, RedirectAttributes redirectAttributes) {
        try {
            LectureEntity lecturer = getCurrentLecturer(principal);
            lectureService.updateProfile(lecturer.getLectureId(), dto);
            redirectAttributes.addFlashAttribute("successMessage", "Профіль успішно оновлено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/profile/edit";
        }
        return "redirect:/user/profile";
    }

    /**
     * Оновлює пароль викладача.
     *
     * @param principal          об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param newPassword        новий пароль
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку профілю
     */
    @PostMapping("/profile/password")
    public String updatePassword(Principal principal, @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        lectureService.updatePassword(lecturer.getLectureId(), newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Пароль успішно змінено!");
        return "redirect:/user/profile";
    }

    /**
     * Оновлює електронну пошту (email) викладача.
     * Якщо email змінено успішно, сесія анулюється і користувач перенаправляється на сторінку логіну.
     *
     * @param principal          об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param newEmail           нова електронна пошта
     * @param request            об'єкт {@link HttpServletRequest} для доступу до поточної сесії
     * @param redirectAttributes об'єкт для передачі flash-повідомлень у разі помилки
     * @return редирект на сторінку профілю, редагування або логіну
     */
    @PostMapping("/profile/email")
    public String updateEmail(Principal principal, @RequestParam String newEmail, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        UserEntity user = lecturer.getUserEntity();

        if (newEmail == null || newEmail.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email не може бути порожнім.");
            return "redirect:/user/profile/edit";
        }

        if (!newEmail.equals(user.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Користувач з таким email вже існує!");
                return "redirect:/user/profile/edit";
            }
            user.setEmail(newEmail);
            userRepository.save(user);

            // Invalidate session because email (username) has changed
            request.getSession().invalidate();
            return "redirect:/login?logout";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Email не змінився.");
        return "redirect:/user/profile";
    }

    /**
     * Відображає сторінку з сертифікатами (документами про підвищення кваліфікації) викладача.
     *
     * @param principal об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param model     об'єкт {@link Model} для передачі даних у представлення
     * @return назва HTML-шаблону сторінки сертифікатів ("userCertificates")
     */
    @GetMapping("/certificates")
    @Transactional(readOnly = true)
    public String certificates(Principal principal, Model model) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        List<UpskillEventEntity> events = upskillEventRepository.findAllByLectureEntity_LectureId(lecturer.getLectureId());

        Map<DisciplineDto, Map<Integer, LecturerManagementController.YearSummary>> groupedEvents = new TreeMap<>(Comparator.comparing(DisciplineDto::getDisciplineName));

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
                            .computeIfAbsent(year, k -> new LecturerManagementController.YearSummary())
                            .addEvent(dto);
                }
            }
        });

        model.addAttribute("lecturer", lecturerMapper.toDto(lecturer));
        model.addAttribute("events", events.stream().map(upskillEventMapper::toDto).collect(Collectors.toList()));
        model.addAttribute("groupedEvents", groupedEvents);
        model.addAttribute("allDisciplines", disciplineRepository.findAll().stream().map(disciplineMapper::toDto).collect(Collectors.toList()));
        return "userCertificates";
    }

    /**
     * Завантажує новий документ (сертифікат) підвищення кваліфікації для поточного викладача.
     *
     * @param principal          об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param file               завантажений файл (документ)
     * @param documentType       тип документу
     * @param disciplineIds      список ідентифікаторів дисциплін, до яких відноситься сертифікат
     * @param eventDto           об'єкт {@link UpskillEventDto} з даними про захід підвищення кваліфікації
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку сертифікатів
     * @throws IOException у разі помилок при читанні файлу
     */
    @PostMapping("/certificates/upload")
    @Transactional
    public String uploadCertificate(Principal principal,
                                    @RequestParam("file") MultipartFile file,
                                    @RequestParam("documentType") String documentType,
                                    @RequestParam(value = "disciplineIds", required = false) List<Long> disciplineIds,
                                    @ModelAttribute UpskillEventDto eventDto,
                                    RedirectAttributes redirectAttributes) throws IOException {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Файл не обрано!");
            return "redirect:/user/certificates";
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Недопустимий формат файлу. Дозволено: PDF, DOC, DOCX, JPEG, PNG.");
            return "redirect:/user/certificates";
        }

        LectureEntity lecturer = getCurrentLecturer(principal);

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
        return "redirect:/user/certificates";
    }

    /**
     * Оновлює інформацію про існуючий документ (сертифікат) підвищення кваліфікації.
     *
     * @param principal          об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param eventId            ідентифікатор заходу (документу), що оновлюється
     * @param file               новий файл (якщо потрібно оновити сам документ)
     * @param documentType       новий тип документу
     * @param eventDetails       оновлені деталі заходу
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку сертифікатів
     * @throws IOException у разі помилок при читанні файлу
     */
    @PostMapping("/certificates/update/{eventId}")
    @Transactional
    public String updateCertificate(Principal principal,
                                    @PathVariable Long eventId,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam("documentType") String documentType,
                                    @ModelAttribute UpskillEventDto eventDetails,
                                    RedirectAttributes redirectAttributes) throws IOException {

        LectureEntity lecturer = getCurrentLecturer(principal);
        UpskillEventEntity existingEvent = upskillEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!existingEvent.getLectureEntity().getLectureId().equals(lecturer.getLectureId())) {
            throw new RuntimeException("Access denied to this event");
        }

        if (file != null && !file.isEmpty()) {
            if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Недопустимий формат файлу. Дозволено: PDF, DOC, DOCX, JPEG, PNG.");
                return "redirect:/user/certificates";
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
        return "redirect:/user/certificates";
    }

    /**
     * Видаляє документ (сертифікат) підвищення кваліфікації викладача за його ідентифікатором.
     *
     * @param principal          об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param eventId            ідентифікатор заходу (документу) для видалення
     * @param redirectAttributes об'єкт для передачі flash-повідомлень
     * @return редирект на сторінку сертифікатів
     */
    @PostMapping("/certificates/delete/{eventId}")
    public String deleteCertificate(Principal principal, @PathVariable Long eventId, RedirectAttributes redirectAttributes) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        UpskillEventEntity existingEvent = upskillEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!existingEvent.getLectureEntity().getLectureId().equals(lecturer.getLectureId())) {
            throw new RuntimeException("Access denied to this event");
        }

        upskillEventRepository.deleteById(eventId);
        redirectAttributes.addFlashAttribute("successMessage", "Документ успішно видалено!");
        return "redirect:/user/certificates";
    }

    /**
     * Завантажує (скачує) файл документу (сертифікату) підвищення кваліфікації.
     *
     * @param principal об'єкт {@link Principal} для ідентифікації поточного користувача
     * @param eventId   ідентифікатор заходу, файл якого потрібно завантажити
     * @return об'єкт {@link ResponseEntity} з масивом байтів файлу для його завантаження
     */
    @GetMapping("/certificates/download/{eventId}")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> downloadCertificate(Principal principal, @PathVariable Long eventId) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        UpskillEventEntity event = upskillEventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Certificate not found"));

        if (!event.getLectureEntity().getLectureId().equals(lecturer.getLectureId())) {
            throw new RuntimeException("Access denied to this certificate");
        }

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
