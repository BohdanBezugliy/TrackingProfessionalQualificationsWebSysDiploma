package com.BezuhlyiBohdanK22_1.qualitrack.mapper;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.EducationDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.EducationEntity;
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
