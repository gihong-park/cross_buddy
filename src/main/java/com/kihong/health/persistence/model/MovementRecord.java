package com.kihong.health.persistence.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import com.kihong.health.persistence.model.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "movement_record")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class MovementRecord extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(targetEntity = Record.class, fetch = FetchType.LAZY)
  @JsonBackReference(value = "record")
  @JoinColumn(name = "record_id")
  private Record record;

  @ManyToOne(targetEntity = WorkoutOftheDay.class, fetch = FetchType.LAZY)
  @JsonBackReference(value = "wod")
  @JoinColumn(name = "wod_id")
  private WorkoutOftheDay wod;

  private String name;

  private int ord;
  @Builder.Default
  private double cal = 0;
  @Builder.Default
  private double distance = 0;
  @Builder.Default
  private int reps = 0;
  @Builder.Default
  private double weight = 0.0;
  @Builder.Default
  private float height = 0;

  public static MovementRecord getValueFrom(CreateMovementRecord cmr) {
    return MovementRecord.builder().name(cmr.getName()).ord(cmr.getOrd()).reps(cmr.getReps()).cal(cmr.getCal())
        .distance(cmr.getDistance()).height(cmr.getHeight()).build();
  }
  public static MovementRecord getValueFrom(MovementRecord mr) {
    return MovementRecord.builder().id(mr.getId()).ord(mr.getOrd()).record(mr.getRecord()).wod(mr.getWod()).name(mr.getName()).reps(mr.getReps()).cal(mr.getCal())
        .distance(mr.getDistance()).height(mr.getHeight()).createdAt(mr.getCreatedAt()).build();
  }
}
