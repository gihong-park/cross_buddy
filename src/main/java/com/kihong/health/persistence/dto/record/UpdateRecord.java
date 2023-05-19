package com.kihong.health.persistence.dto.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kihong.health.persistence.model.MovementRecord;
import jakarta.validation.constraints.NotEmpty;
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
public class UpdateRecord {

  @NotEmpty
  private Long id;
  @JsonProperty("wod_id")
  private Long wodId;
  private LocalDate date;
  private String description;
  private String note;
  @JsonProperty("movement_records")
  private List<MovementRecord> movementRecords;
  private HashMap<String, Object> result;
}
