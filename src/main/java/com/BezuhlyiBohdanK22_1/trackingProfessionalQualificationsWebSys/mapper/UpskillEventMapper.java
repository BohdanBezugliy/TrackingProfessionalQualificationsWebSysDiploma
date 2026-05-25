package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.mapper;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto.UpskillEventDto;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DisciplineEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.UpskillEventEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UpskillEventMapper {

    public UpskillEventDto toDto(UpskillEventEntity entity) {
        if (entity == null) return null;
        UpskillEventDto dto = new UpskillEventDto();
        dto.setId(entity.getUpskillEventId());
        if (entity.getDocumentEntity() != null) {
            dto.setDocumentType(entity.getDocumentEntity().getDocumentType());
        }
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setInstitutionName(entity.getInstitutionName());
        dto.setTopic(entity.getTopic());
        dto.setEctsCredits(entity.getEctsCredits());
        dto.setHours(entity.getHours());
        dto.setDateBegin(entity.getDateBegin());
        dto.setDateEnd(entity.getDateEnd());
        dto.setDateReceived(entity.getDateReceived());
        
        if (entity.getDocumentEntity() != null) {
            dto.setDocumentId(entity.getDocumentEntity().getDocumentId());
            dto.setOriginalFileName(entity.getDocumentEntity().getFileName());
        }
        
        if (entity.getDisciplines() != null) {
            dto.setDisciplineIds(entity.getDisciplines().stream()
                    .map(DisciplineEntity::getDisciplineId)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
}
