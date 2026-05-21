package com.BezuhlyiBohdanK22_1.qualitrack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpskillEventDto {
    private Long id;
    private String documentType;
    private String documentNumber;
    private String institutionName;
    private String topic;
    private BigDecimal ectsCredits;
    private Integer hours;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private LocalDate dateReceived;
    private Long documentId;
    private String originalFileName;
    private List<Long> disciplineIds; // IDs of related disciplines
}
