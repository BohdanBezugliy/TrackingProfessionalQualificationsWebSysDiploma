package com.BezuhlyiBohdanK22_1.qualitrack.repository;

import com.BezuhlyiBohdanK22_1.qualitrack.entity.DisciplineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisciplineRepository extends JpaRepository<DisciplineEntity, Long> {
}
