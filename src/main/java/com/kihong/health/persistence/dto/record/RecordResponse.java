package com.kihong.health.persistence.dto.record;

import com.kihong.health.persistence.dto.user.UserResponse;
import com.kihong.health.persistence.dto.workoutOftheDay.WorkoutOftheDayResponse;
import com.kihong.health.persistence.model.MovementRecord;
import com.kihong.health.persistence.model.Record;
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
public class RecordResponse {

  private Long id;
  private UserResponse user;
  private WorkoutOftheDayResponse wod;

  private LocalDate date;
  private String description;
  private String note;
  private List<MovementRecord> movementRecords;
  private HashMap<String, Object> result;

  static public RecordResponse getValueFrom(Record record) {
    return RecordResponse.builder().id(record.getId())
        .user(UserResponse.getValueFrom(record.getUser())).date(record.getDate())
        .description(record.getDescription())
        .note(record.getNote()).movementRecords(record.getMovementRecords())
        .result(record.getResult())
        .wod(WorkoutOftheDayResponse.getValueFrom(record.getWod())).build();
  }
}
