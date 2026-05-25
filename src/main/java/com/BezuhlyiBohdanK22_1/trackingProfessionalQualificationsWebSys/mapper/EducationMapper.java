package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.EducationDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.EducationEntity;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper {

    public EducationDto toDto(EducationEntity entity) {
        if (entity == null) return null;
        EducationDto dto = new EducationDto();
        dto.setId(entity.getEducationId());
        dto.setInstitutionName(entity.getInstitutionName());
        dto.setSpecialization(entity.getSpecialization());
        dto.setEndingDate(entity.getEndingDate());
        return dto;
    }
}
