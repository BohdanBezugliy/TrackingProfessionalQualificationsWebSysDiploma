package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.DepartmentEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реалізація сервісу для управління кафедрами.
 * Надає базові CRUD операції для роботи із сутностями кафедри.
 */
@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {

    /**
     * Репозиторій для доступу до даних кафедр у базі даних.
     */
    private final DepartmentRepository departmentRepository;

    /**
     * Повертає список усіх кафедр.
     * 
     * @return список об'єктів {@link DepartmentEntity}
     */
    @Override
    public List<DepartmentEntity> findAll() {
        return departmentRepository.findAll();
    }

    /**
     * Шукає кафедру за її ідентифікатором.
     * 
     * @param id ідентифікатор кафедри
     * @return знайдений об'єкт {@link DepartmentEntity}
     * @throws RuntimeException якщо кафедру не знайдено
     */
    @Override
    public DepartmentEntity findById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Department not found"));
    }

    /**
     * Зберігає нову або оновлює існуючу кафедру в базі даних.
     * 
     * @param departmentEntity об'єкт кафедри для збереження
     * @return збережений об'єкт {@link DepartmentEntity}
     */
    @Override
    public DepartmentEntity save(DepartmentEntity departmentEntity) {
        return departmentRepository.save(departmentEntity);
    }

    /**
     * Видаляє кафедру із бази даних за заданим ідентифікатором.
     * 
     * @param id ідентифікатор кафедри
     */
    @Override
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}
