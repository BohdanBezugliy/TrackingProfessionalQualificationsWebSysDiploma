package com.BezuhlyiBohdanK22_1.qualitrack.mapper;

import com.BezuhlyiBohdanK22_1.qualitrack.dto.DisciplineDto;
import com.BezuhlyiBohdanK22_1.qualitrack.entity.DisciplineEntity;
import org.springframework.stereotype.Component;

@Component
public class DisciplineMapper {

    public DisciplineDto toDto(DisciplineEntity entity) {
        if (entity == null) return null;
        DisciplineDto dto = new DisciplineDto();
        dto.setDisciplineId(entity.getDisciplineId());
        dto.setDisciplineName(entity.getDisciplineName());
        dto.setUpskillEventsCount(entity.getUpskillEvents() != null ? entity.getUpskillEvents().size() : 0);
        return dto;
    }
}
