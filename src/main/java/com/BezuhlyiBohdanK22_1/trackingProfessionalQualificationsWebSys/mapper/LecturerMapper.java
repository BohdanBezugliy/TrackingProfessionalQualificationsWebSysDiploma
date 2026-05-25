package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.LecturerDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.LectureEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LecturerMapper {

    private final EducationMapper educationMapper;

    public LecturerDto toDto(LectureEntity entity) {
        if (entity == null) return null;
        LecturerDto dto = new LecturerDto();
        dto.setLectureId(entity.getLectureId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setMiddleName(entity.getMiddleName());
        dto.setAcademicDegree(entity.getAcademicDegree());
        dto.setAcademicRank(entity.getAcademicRank());
        dto.setHireDate(entity.getHireDate());
        
        if (entity.getUserEntity() != null) {
            dto.setEmail(entity.getUserEntity().getEmail());
        }
        
        if (entity.getDepartmentEntity() != null) {
            dto.setDepartmentId(entity.getDepartmentEntity().getDepartmentId());
            dto.setDepartmentName(entity.getDepartmentEntity().getDepartmentName());
            
            if (entity.getDepartmentEntity().getFacultyEntity() != null) {
                dto.setFacultyId(entity.getDepartmentEntity().getFacultyEntity().getFacultyId());
                dto.setFacultyName(entity.getDepartmentEntity().getFacultyEntity().getFacultyName());
            }
        }
        
        if (entity.getEducations() != null) {
            dto.setEducations(entity.getEducations().stream()
                    .map(educationMapper::toDto)
                    .collect(Collectors.toList()));
        }
        
        dto.setTotalHours(entity.getTotalHours());
        dto.setTotalEctsCredits(entity.getTotalEctsCredits());
        
        return dto;
    }
}
