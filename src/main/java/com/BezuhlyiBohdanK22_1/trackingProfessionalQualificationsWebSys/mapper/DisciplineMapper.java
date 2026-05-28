package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DisciplineDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DisciplineEntity;
import org.springframework.stereotype.Component;

/**
 * Маппер для перетворення сутності дисципліни ({@link DisciplineEntity}) у DTO ({@link DisciplineDto}).
 */
@Component
public class DisciplineMapper {

    /**
     * Перетворює сутність дисципліни у DTO.
     *
     * @param entity сутність дисципліни ({@link DisciplineEntity}).
     * @return об'єкт {@link DisciplineDto}, що містить дані дисципліни та кількість пов'язаних заходів підвищення кваліфікації,
     *         або {@code null}, якщо вхідна сутність є {@code null}.
     */
    public DisciplineDto toDto(DisciplineEntity entity) {
        if (entity == null) return null;
        DisciplineDto dto = new DisciplineDto();
        dto.setDisciplineId(entity.getDisciplineId());
        dto.setDisciplineName(entity.getDisciplineName());
        dto.setUpskillEventsCount(entity.getUpskillEvents() != null ? entity.getUpskillEvents().size() : 0);
        return dto;
    }
}
