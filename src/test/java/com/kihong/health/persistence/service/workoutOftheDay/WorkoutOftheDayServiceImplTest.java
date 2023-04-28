package com.kihong.health.persistence.service.workoutOftheDay;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import com.kihong.health.persistence.dto.workoutOftheDay.CreateWorkoutOftheDay;
import com.kihong.health.persistence.model.WorkoutOftheDay;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class WorkoutOftheDayServiceImplTest extends BaseControllerTest {

  @Autowired
  WorkoutOftheDayService wodService;

  @Test
  @DisplayName("CREATE WOD TEST")
  void createWOD() {
    LocalDate date = LocalDate.of(2023, 4, 12);

    CreateMovementRecord cmr = CreateMovementRecord.builder().name("Row").cal(20)
        .build();
    CreateWorkoutOftheDay cod = CreateWorkoutOftheDay.builder().date(date).name("test 1")
        .type("test type 1").description("test description 1").cmrList(
            List.of(cmr)).build();

    WorkoutOftheDay wod = wodService.createWOD(cod);

    assertEquals(date, wod.getDate());
    assertEquals(cod.getName(), wod.getName());
    assertEquals(cod.getType(), wod.getType());
    assertEquals(cod.getDescription(), wod.getDescription());

  }
}