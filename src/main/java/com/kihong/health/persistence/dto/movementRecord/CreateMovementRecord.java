package com.kihong.health.persistence.dto.movementRecord;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMovementRecord {
  @NotEmpty
  private String name;

  private int ord;
  @Builder.Default
  private double cal=0;
  @Builder.Default
  private double distance=0;
  @Builder.Default
  private int reps=0;
  @Builder.Default
  private double weight=0.0;
  @Builder.Default
  private float height=0;
}
