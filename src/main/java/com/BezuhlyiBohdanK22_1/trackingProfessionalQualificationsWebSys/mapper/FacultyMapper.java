package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.FacultyDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.FacultyEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FacultyMapper {

    private final DepartmentMapper departmentMapper;

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
