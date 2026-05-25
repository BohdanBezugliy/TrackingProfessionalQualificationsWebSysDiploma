package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisciplineDto {
    private Long disciplineId;
    private String disciplineName;
    private int upskillEventsCount; // Useful for showing "X пов'язаних документів"
}
