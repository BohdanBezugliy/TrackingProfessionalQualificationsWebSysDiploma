package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.DepartmentDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DepartmentEntity;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

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
