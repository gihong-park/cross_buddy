package com.kihong.health.persistence.dto.workoutOftheDay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kihong.health.persistence.model.MovementRecord;
import com.kihong.health.persistence.model.WorkoutOftheDay;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

@Data
@Builder
@Relation(collectionRelation = "wods", itemRelation = "wod")
public class WorkoutOftheDayResponse {

  private Long id;

  private String name;
  private String type;
  private String description;
  private LocalDate date;
  @JsonProperty("movement_records")
  private List<MovementRecord> movementRecords;
  private HashMap<String, Object> result;


  static public WorkoutOftheDayResponse getValueFrom(WorkoutOftheDay wod) {
    return WorkoutOftheDayResponse.builder().id(wod.getId()).name(wod.getName()).type(wod.getType())
        .description(wod.getDescription()).date(wod.getDate())
        .movementRecords(wod.getMovementRecords()).result(wod.getResult()).build();
  }
}
