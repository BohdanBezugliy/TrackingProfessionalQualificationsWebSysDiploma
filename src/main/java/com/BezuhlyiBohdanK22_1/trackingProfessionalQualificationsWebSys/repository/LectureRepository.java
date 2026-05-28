package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.repository;

import com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторій для доступу до даних викладачів.
 * Забезпечує виконання базових CRUD операцій та спеціалізованих запитів для сутності {@link LectureEntity}.
 */
public interface LectureRepository extends JpaRepository<LectureEntity, Long> {

    /**
     * Знаходить викладача за електронною адресою пов'язаного користувача.
     *
     * @param email електронна адреса користувача.
     * @return {@link Optional}, що містить сутність викладача, якщо такого знайдено, інакше - порожній.
     */
    @Query("SELECT l FROM LectureEntity l WHERE l.userEntity.email = :email")
    Optional<LectureEntity> findByUserEmail(@Param("email") String email);

    /**
     * Виконує пошук викладачів за ключовим словом (ПІБ або email), а також з фільтрацією за факультетом та кафедрою
     * з підтримкою сторінкового виводу.
     *
     * @param keywordPattern шаблон ключового слова для пошуку у нижньому регістрі (може бути null).
     * @param facultyId      ідентифікатор факультету для фільтрації (може бути null).
     * @param departmentId   ідентифікатор кафедри для фільтрації (може бути null).
     * @param pageable       об'єкт, що містить інформацію про пагінацію та сортування.
     * @return сторінка з результатами пошуку викладачів.
     */
    @Query("SELECT l FROM LectureEntity l WHERE " +
           "(:keywordPattern IS NULL OR LOWER(l.firstName) LIKE :keywordPattern " +
           "OR LOWER(l.lastName) LIKE :keywordPattern " +
           "OR LOWER(l.middleName) LIKE :keywordPattern " +
           "OR LOWER(l.userEntity.email) LIKE :keywordPattern) " +
           "AND (:facultyId IS NULL OR l.departmentEntity.facultyEntity.facultyId = :facultyId) " +
           "AND (:departmentId IS NULL OR l.departmentEntity.departmentId = :departmentId)")
    org.springframework.data.domain.Page<LectureEntity> searchLecturers(@Param("keywordPattern") String keywordPattern, 
                                        @Param("facultyId") Long facultyId, 
                                        @Param("departmentId") Long departmentId,
                                        org.springframework.data.domain.Pageable pageable);
}
