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

@Controller
@RequestMapping("/admin/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ExportService exportService;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    @GetMapping
    public String showReportGenerator(Model model) {
        model.addAttribute("faculties", facultyRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        return "reports";
    }

    @GetMapping("/view")
    public String viewReport(
            @RequestParam(defaultValue = "UNIVERSITY") String level,
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo,
            Model model) {
        
        List<ReportLecturerDto> reportData = reportService.generateReport(level, facultyId, departmentId, yearFrom, yearTo);
        
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
        
        return "reports :: reportTable";
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf(
            @RequestParam(defaultValue = "UNIVERSITY") String level,
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo) throws Exception {

        List<ReportLecturerDto> data = reportService.generateReport(level, facultyId, departmentId, yearFrom, yearTo);
        byte[] pdfBytes = exportService.generatePdfReport(data, level, yearFrom, yearTo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @GetMapping("/xlsx")
    public ResponseEntity<byte[]> downloadXlsx(
            @RequestParam(defaultValue = "UNIVERSITY") String level,
            @RequestParam(required = false) Long facultyId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Integer yearFrom,
            @RequestParam(required = false) Integer yearTo) throws Exception {

        List<ReportLecturerDto> data = reportService.generateReport(level, facultyId, departmentId, yearFrom, yearTo);
        byte[] excelBytes = exportService.generateXlsxReport(data, level, yearFrom, yearTo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelBytes);
    }
}
