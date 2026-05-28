package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DepartmentDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DepartmentEntity;
import org.springframework.stereotype.Component;

/**
 * Маппер для перетворення сутності кафедри ({@link DepartmentEntity}) у DTO ({@link DepartmentDto}).
 */
@Component
public class DepartmentMapper {

    /**
     * Перетворює сутність кафедри у DTO.
     *
     * @param entity сутність кафедри ({@link DepartmentEntity}).
     * @return об'єкт {@link DepartmentDto}, що містить дані кафедри та пов'язаного факультету,
     *         або {@code null}, якщо вхідна сутність є {@code null}.
     */
    public DepartmentDto toDto(DepartmentEntity entity) {
        if (entity == null) return null;
        DepartmentDto dto = new DepartmentDto();
        dto.setDepartmentId(entity.getDepartmentId());
        dto.setDepartmentName(entity.getDepartmentName());
        if (entity.getFacultyEntity() != null) {
            dto.setFacultyId(entity.getFacultyEntity().getFacultyId());
            dto.setFacultyName(entity.getFacultyEntity().getFacultyName());
        }
        return dto;
    }
}
