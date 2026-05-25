package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.ReportLecturerDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.*;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final LectureRepository lectureRepository;
    private final UpskillEventRepository upskillEventRepository;

    @Transactional(readOnly = true)
    public List<ReportLecturerDto> generateReport(String level, Long facultyId, Long departmentId, Integer yearFrom, Integer yearTo) {
        List<LectureEntity> lecturers;

        // Fetch based on level
        if ("DEPARTMENT".equalsIgnoreCase(level) && departmentId != null) {
            lecturers = lectureRepository.findAll().stream()
                    .filter(l -> l.getDepartmentEntity() != null && l.getDepartmentEntity().getDepartmentId().equals(departmentId))
                    .collect(Collectors.toList());
        } else if ("FACULTY".equalsIgnoreCase(level) && facultyId != null) {
            lecturers = lectureRepository.findAll().stream()
                    .filter(l -> l.getDepartmentEntity() != null && l.getDepartmentEntity().getFacultyEntity() != null 
                            && l.getDepartmentEntity().getFacultyEntity().getFacultyId().equals(facultyId))
                    .collect(Collectors.toList());
        } else {
            // UNIVERSITY level
            lecturers = lectureRepository.findAll();
        }

        List<ReportLecturerDto> reportData = new ArrayList<>();
        
        for (LectureEntity lecturer : lecturers) {
            // Fetch events manually for transaction safety if lazy loading issues arise, 
            // but since we're in @Transactional they can be accessed.
            List<UpskillEventEntity> allEvents = upskillEventRepository.findAllByLectureEntity_LectureId(lecturer.getLectureId());
            
            // Filter by year
            List<UpskillEventEntity> filteredEvents = allEvents.stream().filter(e -> {
                if (e.getDateEnd() == null) return false;
                int year = e.getDateEnd().getYear();
                boolean afterStart = (yearFrom == null) || (year >= yearFrom);
                boolean beforeEnd = (yearTo == null) || (year <= yearTo);
                return afterStart && beforeEnd;
            }).collect(Collectors.toList());

            if (!filteredEvents.isEmpty() || yearFrom == null) {
                reportData.add(mapToReportDto(lecturer, filteredEvents));
            }
        }
        
        // Sort by Faculty Name, then Department Name, then Lecturer Name
        reportData.sort((a, b) -> {
            int facCmp = safeString(a.getFacultyName()).compareTo(safeString(b.getFacultyName()));
            if (facCmp != 0) return facCmp;
            int depCmp = safeString(a.getDepartmentName()).compareTo(safeString(b.getDepartmentName()));
            if (depCmp != 0) return depCmp;
            return safeString(a.getFullName()).compareTo(safeString(b.getFullName()));
        });

        return reportData;
    }

    private String safeString(String s) {
        return s == null ? "" : s;
    }

    private ReportLecturerDto mapToReportDto(LectureEntity l, List<UpskillEventEntity> events) {
        ReportLecturerDto dto = new ReportLecturerDto();
        dto.setId(l.getLectureId());
        
        String fullName = l.getLastName() + " " + l.getFirstName() + (l.getMiddleName() != null ? " " + l.getMiddleName() : "");
        dto.setFullName(fullName);
        
        if (l.getDepartmentEntity() != null) {
            dto.setDepartmentName(l.getDepartmentEntity().getDepartmentName());
            if (l.getDepartmentEntity().getFacultyEntity() != null) {
                dto.setFacultyName(l.getDepartmentEntity().getFacultyEntity().getFacultyName());
            }
        }
        
        String academicRank = l.getAcademicRank() != null ? l.getAcademicRank() : "";
        String academicDegree = l.getAcademicDegree() != null ? l.getAcademicDegree() : "";
        
        // Use academic rank as position if position doesn't exist
        String position = academicRank.isEmpty() ? "Викладач" : academicRank;
        dto.setPosition(position);
        
        // Academic Info
        StringBuilder acadInfo = new StringBuilder();
        if (!academicDegree.isEmpty()) acadInfo.append("Науковий ступінь: ").append(academicDegree).append("\n");
        if (!academicRank.isEmpty()) acadInfo.append("Вчене звання: ").append(academicRank);
        dto.setAcademicInfo(acadInfo.toString().trim());

        // Education
        if (l.getEducations() != null && !l.getEducations().isEmpty()) {
            String eduStr = l.getEducations().stream()
                    .map(e -> String.format("%s, %s, рік закінчення: %d", 
                            e.getSpecialization(), e.getInstitutionName(), e.getEndingDate().getYear()))
                    .collect(Collectors.joining(";\n"));
            dto.setEducationDetails(eduStr);
        } else {
            dto.setEducationDetails("Немає даних");
        }

        // Upskilling
        if (events != null && !events.isEmpty()) {
            String upStr = events.stream()
                    .map(e -> {
                        String type = e.getDocumentEntity() != null ? e.getDocumentEntity().getDocumentType() : "Документ";
                        return String.format("%d р. - %s, %s, %d год (%.1f кредити ЄКТС)", 
                                e.getDateEnd().getYear(), e.getInstitutionName(), e.getTopic(), 
                                e.getHours(), e.getEctsCredits());
                    })
                    .collect(Collectors.joining(";\n"));
            dto.setUpskillingDetails(upStr);
        } else {
            dto.setUpskillingDetails("Немає даних за обраний період");
        }

        return dto;
    }
}
