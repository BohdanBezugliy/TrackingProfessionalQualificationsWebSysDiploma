package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.ReportLecturerDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.*;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final LectureRepository lectureRepository;
    private final UpskillEventRepository upskillEventRepository;

    @Transactional(readOnly = true)
    public List<ReportLecturerDto> generateReport(String level, Long facultyId, Long departmentId, Long lecturerId, Long disciplineId, Integer yearFrom, Integer yearTo, boolean detailedMode) {
        List<LectureEntity> lecturers;

        // Fetch based on level
        if ("LECTURER".equalsIgnoreCase(level) && lecturerId != null) {
            lecturers = lectureRepository.findById(lecturerId).stream().collect(Collectors.toList());
        } else if ("DEPARTMENT".equalsIgnoreCase(level) && departmentId != null) {
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
            
            // Filter by year and discipline
            List<UpskillEventEntity> filteredEvents = allEvents.stream().filter(e -> {
                if (e.getDateEnd() == null) return false;
                int year = e.getDateEnd().getYear();
                boolean afterStart = (yearFrom == null) || (year >= yearFrom);
                boolean beforeEnd = (yearTo == null) || (year <= yearTo);
                
                boolean matchesDiscipline = true;
                if (disciplineId != null) {
                    matchesDiscipline = e.getDisciplines() != null && e.getDisciplines().stream()
                            .anyMatch(d -> d.getDisciplineId().equals(disciplineId));
                }
                
                return afterStart && beforeEnd && matchesDiscipline;
            }).collect(Collectors.toList());

            if (!filteredEvents.isEmpty() || (yearFrom == null && disciplineId == null)) {
                if (detailedMode) {
                    // Collect all unique disciplines across the filtered events
                    Set<DisciplineEntity> disciplines = new HashSet<>();
                    for (UpskillEventEntity event : filteredEvents) {
                        if (event.getDisciplines() != null && !event.getDisciplines().isEmpty()) {
                            disciplines.addAll(event.getDisciplines());
                        }
                    }
                    
                    if (disciplineId != null) {
                        disciplines.removeIf(d -> !d.getDisciplineId().equals(disciplineId));
                    }
                    
                    if (disciplines.isEmpty()) {
                        reportData.add(mapToReportDto(lecturer, filteredEvents, "Без прив'язки до дисципліни"));
                    } else {
                        // Create a row for each discipline
                        for (DisciplineEntity discipline : disciplines) {
                            List<UpskillEventEntity> discEvents = filteredEvents.stream()
                                    .filter(e -> e.getDisciplines() != null && e.getDisciplines().contains(discipline))
                                    .collect(Collectors.toList());
                            reportData.add(mapToReportDto(lecturer, discEvents, discipline.getDisciplineName()));
                        }
                    }
                } else {
                    reportData.add(mapToReportDto(lecturer, filteredEvents, null));
                }
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

    @Transactional(readOnly = true)
    public List<com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DisciplineDto> getAvailableDisciplines(String level, Long facultyId, Long departmentId, Long lecturerId) {
        List<LectureEntity> lecturers;

        if ("LECTURER".equalsIgnoreCase(level) && lecturerId != null) {
            lecturers = lectureRepository.findById(lecturerId).stream().collect(Collectors.toList());
        } else if ("DEPARTMENT".equalsIgnoreCase(level) && departmentId != null) {
            lecturers = lectureRepository.findAll().stream()
                    .filter(l -> l.getDepartmentEntity() != null && l.getDepartmentEntity().getDepartmentId().equals(departmentId))
                    .collect(Collectors.toList());
        } else if ("FACULTY".equalsIgnoreCase(level) && facultyId != null) {
            lecturers = lectureRepository.findAll().stream()
                    .filter(l -> l.getDepartmentEntity() != null && l.getDepartmentEntity().getFacultyEntity() != null 
                            && l.getDepartmentEntity().getFacultyEntity().getFacultyId().equals(facultyId))
                    .collect(Collectors.toList());
        } else {
            lecturers = lectureRepository.findAll();
        }

        Set<DisciplineEntity> disciplines = new HashSet<>();
        for (LectureEntity lecturer : lecturers) {
            List<UpskillEventEntity> events = upskillEventRepository.findAllByLectureEntity_LectureId(lecturer.getLectureId());
            for (UpskillEventEntity event : events) {
                if (event.getDisciplines() != null) {
                    disciplines.addAll(event.getDisciplines());
                }
            }
        }

        return disciplines.stream()
                .map(d -> new com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DisciplineDto(d.getDisciplineId(), d.getDisciplineName(), 0))
                .sorted((a, b) -> a.getDisciplineName().compareToIgnoreCase(b.getDisciplineName()))
                .collect(Collectors.toList());
    }

    private String safeString(String s) {
        return s == null ? "" : s;
    }

    private ReportLecturerDto mapToReportDto(LectureEntity l, List<UpskillEventEntity> events, String disciplineName) {
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

        // Discipline details
        dto.setDisciplineDetails(disciplineName != null ? disciplineName : "");

        // Upskilling
        if (events != null && !events.isEmpty()) {
            String upStr = events.stream()
                    .map(e -> {
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
