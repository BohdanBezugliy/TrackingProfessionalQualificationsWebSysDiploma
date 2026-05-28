package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.EducationDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.EducationEntity;
import org.springframework.stereotype.Component;

/**
 * Маппер для перетворення сутності освіти ({@link EducationEntity}) у DTO ({@link EducationDto}).
 */
@Component
public class EducationMapper {

    /**
     * Перетворює сутність освіти у DTO.
     *
     * @param entity сутність освіти ({@link EducationEntity}).
     * @return об'єкт {@link EducationDto}, що містить дані про освіту,
     *         або {@code null}, якщо вхідна сутність є {@code null}.
     */
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
