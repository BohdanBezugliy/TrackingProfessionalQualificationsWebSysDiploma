package com.BezuhlyiBohdanK22_1.qualitrack.controller;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.DisciplineDto;
import com.BezuhlyiBohdanK22_1.qualitrack.dto.LecturerDto;
import com.BezuhlyiBohdanK22_1.qualitrack.dto.UpskillEventDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.*;
import com.BezuhlyiBohdanK22_1.qualitrack.mapper.DepartmentMapper;
import com.BezuhlyiBohdanK22_1.qualitrack.mapper.DisciplineMapper;
import com.BezuhlyiBohdanK22_1.qualitrack.mapper.LecturerMapper;
import com.BezuhlyiBohdanK22_1.qualitrack.mapper.UpskillEventMapper;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DepartmentRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DisciplineRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.DocumentRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.LectureRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.UpskillEventRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.repository.UserRepository;
import com.BezuhlyiBohdanK22_1.qualitrack.service.ILectureService;
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

    private LectureEntity getCurrentLecturer(Principal principal) {
        if (principal == null) throw new RuntimeException("User not authenticated");
        return lectureRepository.findByUserEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Профіль викладача не знайдено для поточного користувача"));
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/user/profile";
    }

    @GetMapping("/profile")
    @Transactional(readOnly = true)
    public String profile(Principal principal, Model model) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        model.addAttribute("lecturer", lecturerMapper.toDto(lecturer));
        return "userProfile";
    }

    @GetMapping("/profile/edit")
    @Transactional(readOnly = true)
    public String editProfile(Principal principal, Model model) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        model.addAttribute("lecturer", lecturerMapper.toDto(lecturer));
        model.addAttribute("departments", departmentRepository.findAll().stream().map(departmentMapper::toDto).collect(Collectors.toList()));
        return "userEditProfile";
    }

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

    @PostMapping("/profile/password")
    public String updatePassword(Principal principal, @RequestParam String newPassword, RedirectAttributes redirectAttributes) {
        LectureEntity lecturer = getCurrentLecturer(principal);
        lectureService.updatePassword(lecturer.getLectureId(), newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "Пароль успішно змінено!");
        return "redirect:/user/profile";
    }

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
