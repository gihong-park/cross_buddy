package com.kihong.health.persistence.dto.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import jakarta.validation.constraints.NotNull;
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
public class CreateRecord {

  @JsonProperty("wod_id")
  private Long wodId;

  @NotNull
  private LocalDate date;
  private String description;
  private String note;
  @JsonProperty("movement_records")
  private List<CreateMovementRecord> movementRecords;
  private HashMap<String, Object> result;
}
