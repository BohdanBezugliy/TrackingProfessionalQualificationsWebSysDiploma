package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.service;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.FacultyEntity;
import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Реалізація сервісу для управління факультетами.
 * Надає базові CRUD операції для роботи із сутностями факультету.
 */
@Service
@RequiredArgsConstructor
public class FacultyService implements IFacultyService {

    /**
     * Репозиторій для доступу до даних факультетів у базі даних.
     */
    private final FacultyRepository facultyRepository;

    /**
     * Повертає список усіх факультетів.
     * 
     * @return список об'єктів {@link FacultyEntity}
     */
    @Override
    public List<FacultyEntity> findAll() {
        return facultyRepository.findAll();
    }

    /**
     * Шукає факультет за його ідентифікатором.
     * 
     * @param id ідентифікатор факультету
     * @return знайдений об'єкт {@link FacultyEntity}
     * @throws RuntimeException якщо факультет не знайдено
     */
    @Override
    public FacultyEntity findById(Long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new RuntimeException("Faculty not found"));
    }

    /**
     * Зберігає новий або оновлює існуючий факультет у базі даних.
     * 
     * @param facultyEntity об'єкт факультету для збереження
     * @return збережений об'єкт {@link FacultyEntity}
     */
    @Override
    public FacultyEntity save(FacultyEntity facultyEntity) {
        return facultyRepository.save(facultyEntity);
    }

    /**
     * Видаляє факультет із бази даних за заданим ідентифікатором.
     * 
     * @param id ідентифікатор факультету
     */
    @Override
    public void deleteById(Long id) {
        facultyRepository.deleteById(id);
    }
}
