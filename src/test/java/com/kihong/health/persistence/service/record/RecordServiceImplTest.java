package com.kihong.health.persistence.service.record;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import com.kihong.health.persistence.dto.record.CreateRecord;
import com.kihong.health.persistence.model.Record;
import com.kihong.health.persistence.repository.MovementRecordRepository;
import com.kihong.health.persistence.repository.UserRepository;
import com.kihong.health.persistence.repository.WorkoutOftheDayRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RecordServiceImplTest extends BaseControllerTest {

  @Autowired
  private RecordService recordService;

  @Test
  @DisplayName("CREATE RECORD TEST")
  void createRecordTest() {
    CreateMovementRecord createMovementRecord = CreateMovementRecord.builder().name("Row").cal(20)
        .build();

    CreateRecord cr = CreateRecord.builder().date(LocalDate.now()).wodId(Long.valueOf(1))
        .movementRecords(
            List.of(createMovementRecord)).build();

    Record record = recordService.createRecord(cr);

    assertEquals(4, record.getId());
  }

  @Test
  @DisplayName("GET RECORD TEST")
  void getRecordTest() {
    Record record = recordService.getRecord(Long.valueOf(1));

    assertEquals(1, record.getId());
  }


}