package com.kihong.health.persistence.repository;

import com.kihong.health.persistence.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecordRepository extends JpaRepository<Record, Long> {

  @Query(value = "SELECT r FROM Record r JOIN FETCH r.movementRecords mr WHERE mr.name = :name", countQuery = "SELECT COUNT(r) FROM Record r JOIN r.movementRecords mr WHERE mr.name = :name")
  Page<Record> findRecordsByMovementRecordName(@Param("name") String name, Pageable pageable);

  Page<Record> findByUser_id(Long id, Pageable pageable);
  @Query(value = "SELECT r FROM Record r JOIN FETCH r.movementRecords mr WHERE mr.name = :name and r.user.id = :user_id", countQuery = "SELECT COUNT(r) FROM Record r JOIN r.movementRecords mr WHERE mr.name = :name and r.user.id = :user_id")
  Page<Record> findRecordsByMovementRecordNameByUser_id(@Param("name") String name, @Param("user_id") Long userId, Pageable pageable);
}
