package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) для повної інформації про викладача.
 * Використовується для відображення детального профілю викладача,
 * включаючи сумарну кількість годин та кредитів підвищення кваліфікації.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDto {

    /**
     * Унікальний ідентифікатор викладача.
     */
    private Long lectureId;

    /**
     * Ім'я викладача.
     */
    private String firstName;

    /**
     * По батькові викладача.
     */
    private String middleName;

    /**
     * Прізвище викладача.
     */
    private String lastName;

    /**
     * Контактна електронна адреса викладача.
     */
    private String email;

    /**
     * Науковий ступінь викладача.
     */
    private String academicDegree;

    /**
     * Вчене звання викладача.
     */
    private String academicRank;
    
    /**
     * Дата прийняття викладача на роботу.
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;
    
    /**
     * Ідентифікатор кафедри, за якою закріплений викладач.
     */
    private Long departmentId;

    /**
     * Назва кафедри, за якою закріплений викладач.
     */
    private String departmentName;
    
    /**
     * Ідентифікатор факультету, до якого належить викладач.
     */
    private Long facultyId;

    /**
     * Назва факультету, до якого належить викладач.
     */
    private String facultyName;
    
    /**
     * Список записів про отриману освіту викладача.
     */
    private List<EducationDto> educations;
    
    /**
     * Сумарна кількість годин підвищення кваліфікації, пройдених викладачем.
     */
    private Integer totalHours;

    /**
     * Сумарна кількість кредитів ЄКТС, отриманих викладачем за заходи підвищення кваліфікації.
     */
    private java.math.BigDecimal totalEctsCredits;
}
