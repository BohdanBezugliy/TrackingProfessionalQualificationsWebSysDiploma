package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.controller;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.ReportLecturerDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.DepartmentRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.FacultyRepository;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service.ReportService;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * Контролер для генерації та перегляду звітів адміністратором.
 * Відповідає за формування звітів щодо підвищення кваліфікації викладачів,
 * а також за їх експорт у формати PDF та XLSX (Excel).
 */
@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ExportService exportService;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    private final com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.LectureRepository lectureRepository;
    private final com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.DisciplineRepository disciplineRepository;

    /**
     * Відображає сторінку генератора звітів з фільтрами по факультетах, кафедрах, викладачах та дисциплінах.
     *
     * @param model об'єкт {@link Model} для передачі довідкових даних у форму звіту
     * @return назва HTML-шаблону сторінки звітів ("reports")
     */
    @GetMapping
    public String showReportGenerator(Model model) {
        model.addAttribute("faculties", facultyRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("lecturers", lectureRepository.findAll());
        model.addAttribute("disciplines", disciplineRepository.findAll());
        return "reports";
    }

    /**
     * Генерує та повертає фрагмент HTML-таблиці з результатами звіту на основі переданих фільтрів.
     * Використовується для асинхронного (AJAX) оновлення таблиці звітів на сторінці.
     *
     * @param level        рівень звіту ("UNIVERSITY", "FACULTY", "DEPARTMENT", "LECTURER")
     * @param facultyId    ідентифікатор факультету (якщо застосовується)
     * @param departmentId ідентифікатор кафедри (якщо застосовується)
     * @param lecturerId   ідентифікатор викладача (якщо застосовується)
     * @param disciplineId ідентифікатор дисципліни для фільтрації (необов'язково)
     * @param yearFrom     початковий рік періоду для звіту (необов'язково)
     * @param yearTo       кінцевий рік періоду для звіту (необов'язково)
     * @param detailedMode чи використовувати деталізований режим (показувати всі заходи) або зведений
     * @param model        об'єкт {@link Model} для передачі згрупованих даних та налаштувань у представлення
     * @return фрагмент HTML-шаблону з таблицею звітів ("reports :: reportTable")
     */
    @GetMapping("/view")
    public String viewReport(
            @RequestParam(defaultValue = "UNIVERSITY") String level,
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long lecturerId,
            @RequestParam(required = false) Long disciplineId,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(defaultValue = "false") boolean detailedMode,
            Model model) {
        
        List<ReportLecturerDto> reportData = reportService.generateReport(level, facultyId, departmentId, lecturerId, disciplineId, yearFrom, yearTo, detailedMode);
        
        Map<String, List<ReportLecturerDto>> groupedData = reportData.stream()
                .collect(Collectors.groupingBy(
                        r -> (r.getFacultyName() != null ? r.getFacultyName() : "") + " / " + (r.getDepartmentName() != null ? r.getDepartmentName() : ""),
                        LinkedHashMap::new, 
                        Collectors.toList()
                ));
        
        model.addAttribute("groupedData", groupedData);
        model.addAttribute("reportData", reportData); // keep for emptiness check if needed
        model.addAttribute("level", level);
        model.addAttribute("yearFrom", yearFrom);
        model.addAttribute("yearTo", yearTo);
        model.addAttribute("detailedMode", detailedMode);
        
        return "reports :: reportTable";
    }

    /**
     * Отримує список доступних дисциплін на основі вибраних фільтрів (рівня, факультету, кафедри, викладача).
     * Використовується для динамічного оновлення списку дисциплін у формі (через AJAX).
     *
     * @param level        рівень звіту
     * @param facultyId    ідентифікатор факультету
     * @param departmentId ідентифікатор кафедри
     * @param lecturerId   ідентифікатор викладача
     * @return список об'єктів {@link com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DisciplineDto} у форматі JSON
     */
    @GetMapping("/disciplines")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DisciplineDto> getAvailableDisciplines(
            @RequestParam(defaultValue = "UNIVERSITY") String level,
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long lecturerId) {
        return reportService.getAvailableDisciplines(level, facultyId, departmentId, lecturerId);
    }

    /**
     * Генерує та завантажує звіт у форматі PDF на основі вибраних фільтрів.
     *
     * @param level        рівень звіту
     * @param facultyId    ідентифікатор факультету
     * @param departmentId ідентифікатор кафедри
     * @param lecturerId   ідентифікатор викладача
     * @param disciplineId ідентифікатор дисципліни
     * @param yearFrom     початковий рік
     * @param yearTo       кінцевий рік
     * @param detailedMode чи використовувати деталізований режим
     * @return об'єкт {@link ResponseEntity} з масивом байтів PDF-файлу для скачування
     * @throws Exception у разі помилки при генерації PDF
     */
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @RequestParam(defaultValue = "UNIVERSITY") String level,
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long lecturerId,
            @RequestParam(required = false) Long disciplineId,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(defaultValue = "false") boolean detailedMode) throws Exception {

        List<ReportLecturerDto> data = reportService.generateReport(level, facultyId, departmentId, lecturerId, disciplineId, yearFrom, yearTo, detailedMode);
        byte[] pdfBytes = exportService.generatePdfReport(data, level, yearFrom, yearTo, detailedMode);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    /**
     * Генерує та завантажує звіт у форматі XLSX (Excel) на основі вибраних фільтрів.
     *
     * @param level        рівень звіту
     * @param facultyId    ідентифікатор факультету
     * @param departmentId ідентифікатор кафедри
     * @param lecturerId   ідентифікатор викладача
     * @param disciplineId ідентифікатор дисципліни
     * @param yearFrom     початковий рік
     * @param yearTo       кінцевий рік
     * @param detailedMode чи використовувати деталізований режим
     * @return об'єкт {@link ResponseEntity} з масивом байтів Excel-файлу для скачування
     * @throws Exception у разі помилки при генерації XLSX
     */
    @GetMapping("/xlsx")
    public ResponseEntity<byte[]> downloadXlsx(
            @RequestParam(defaultValue = "UNIVERSITY") String level,
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Long lecturerId,
            @RequestParam(required = false) Long disciplineId,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            @RequestParam(defaultValue = "false") boolean detailedMode) throws Exception {

        List<ReportLecturerDto> data = reportService.generateReport(level, facultyId, departmentId, lecturerId, disciplineId, yearFrom, yearTo, detailedMode);
        byte[] excelBytes = exportService.generateXlsxReport(data, level, yearFrom, yearTo, detailedMode);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
