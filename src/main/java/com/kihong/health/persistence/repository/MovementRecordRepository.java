package com.kihong.health.persistence.repository;

import com.kihong.health.persistence.model.MovementRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRecordRepository extends JpaRepository<MovementRecord, Long> {

}
