package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.FacultyDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.FacultyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Маппер для перетворення сутності факультету ({@link FacultyEntity}) у DTO ({@link FacultyDto}).
 */
@Component
@RequiredArgsConstructor
public class FacultyMapper {

    private final DepartmentMapper departmentMapper;

    /**
     * Перетворює сутність факультету у DTO.
     *
     * @param entity сутність факультету ({@link FacultyEntity}).
     * @return об'єкт {@link FacultyDto}, що містить дані факультету та список його кафедр,
     *         або {@code null}, якщо вхідна сутність є {@code null}.
     */
    public FacultyDto toDto(FacultyEntity entity) {
        if (entity == null) return null;
        FacultyDto dto = new FacultyDto();
        dto.setFacultyId(entity.getFacultyId());
        dto.setFacultyName(entity.getFacultyName());
        if (entity.getDepartments() != null) {
            dto.setDepartments(entity.getDepartments().stream()
                    .map(departmentMapper::toDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
