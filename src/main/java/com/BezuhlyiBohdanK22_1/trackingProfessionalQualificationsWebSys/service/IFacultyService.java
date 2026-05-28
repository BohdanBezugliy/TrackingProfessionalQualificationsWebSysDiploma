package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.FacultyEntity;
import java.util.List;

/**
 * Інтерфейс сервісу для управління факультетами.
 * Визначає контракти для отримання, збереження та видалення інформації про факультети.
 */
public interface IFacultyService {
    
    /**
     * Отримує список усіх факультетів, зареєстрованих у системі.
     * 
     * @return список сутностей {@link FacultyEntity}
     */
    List<FacultyEntity> findAll();

    /**
     * Отримує факультет за його унікальним ідентифікатором.
     * 
     * @param id ідентифікатор факультету
     * @return знайдена сутність {@link FacultyEntity} або null
     */
    FacultyEntity findById(Long id);

    /**
     * Зберігає новий або оновлює існуючий факультет.
     * 
     * @param facultyEntity сутність факультету для збереження
     * @return збережена сутність {@link FacultyEntity}
     */
    FacultyEntity save(FacultyEntity facultyEntity);

    /**
     * Видаляє факультет за його ідентифікатором.
     * 
     * @param id ідентифікатор факультету для видалення
     */
    void deleteById(Long id);
}
