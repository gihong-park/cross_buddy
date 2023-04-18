package com.kihong.health.persistence.dto.workoutOftheDay;

import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkoutOftheDay {

  private String name;
  private String type;
  private String description;
  private LocalDate date;
  private List<CreateMovementRecord> cmrList;
  private HashMap<String, Object> result;
}
