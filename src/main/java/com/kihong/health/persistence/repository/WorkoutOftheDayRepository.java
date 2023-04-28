package com.kihong.health.persistence.repository;

import com.kihong.health.persistence.model.WorkoutOftheDay;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutOftheDayRepository extends JpaRepository<WorkoutOftheDay, Long> {

  Page<WorkoutOftheDay> findByDateBetweenOrderByDate(LocalDate startDate, LocalDate endDate,
      Pageable pageable);
}
