package com.kihong.health.web.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import com.kihong.health.persistence.dto.record.CreateRecord;
import com.kihong.health.persistence.dto.record.UpdateRecord;
import com.kihong.health.persistence.model.MovementRecord;
import com.kihong.health.persistence.model.Record;
import com.kihong.health.persistence.model.User.Role;
import com.kihong.health.persistence.repository.MovementRecordRepository;
import com.kihong.health.persistence.repository.UserRepository;
import com.kihong.health.persistence.repository.WorkoutOftheDayRepository;
import com.kihong.health.persistence.service.record.RecordService;
import com.kihong.health.web.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class RecordControllerTest extends BaseControllerTest {

  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  UserRepository userRepository;
  @Autowired
  WorkoutOftheDayRepository workoutOftheDayRepository;
  @Autowired
  MovementRecordRepository movementRecordRepository;
  @Autowired
  RecordService recordService;

  @Test
  @DisplayName("LIST RECORD TEST")
  void listRecord() throws Exception {
    ResultActions result = this.mockMvc.perform(
        get("/api/v1/record").header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN)));

    ResultActions expect = result.andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(1))
        .andExpect(jsonPath("$.content[0].wod.name").value("WOD 1"))
        .andExpect(jsonPath("$.content[0].result").exists())
        .andExpect(jsonPath("$.content[0].movementRecords[?(@.name == 'Row')]").exists())
        .andExpect(jsonPath("$.content[1].id").value(2))
        .andExpect(jsonPath("$.content[1].wod.name").value("WOD 2"))
        .andExpect(jsonPath("$.content[1].movementRecords[?(@.name == 'Row')]").exists());
  }

  @Test
  @DisplayName("FORBIDDEN LIST RECORD")
  void forbiddenListRecord() throws Exception {
    ResultActions result = this.mockMvc.perform(
        get("/api/v1/record").param("id", "1").header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.USER)));

    ResultActions expect = result.andDo(print()).andExpect(status().isForbidden())
        .andExpect(jsonPath("timestamp").exists())
        .andExpect(jsonPath("status").value(ErrorCode.USER_FORBIDDEN.getHttpStatus().value()))
        .andExpect(jsonPath("error").value(ErrorCode.USER_FORBIDDEN.getHttpStatus().name()))
        .andExpect(jsonPath("code").value(ErrorCode.USER_FORBIDDEN.getErrorCode()))
        .andExpect(jsonPath("message").value("User Forbidden"))
        .andExpect(jsonPath("details").value(ErrorCode.USER_FORBIDDEN.getDetail()))
        ;
  }

  @Test
  @DisplayName("SEARCH RECORD TEST")
  void searchRecord() throws Exception {
    ResultActions result = this.mockMvc.perform(
        get("/api/v1/record").header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN))
            .param("search", "row"));

    ResultActions expect = result.andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(1))
        .andExpect(jsonPath("$.content[0].wod.name").value("WOD 1"))
        .andExpect(jsonPath("$.content[0].result").exists())
        .andExpect(jsonPath("$.content[0].movementRecords[?(@.name == 'Row')]").exists())
        .andExpect(jsonPath("$.content[1].id").value(2))
        .andExpect(jsonPath("$.content[1].wod.name").value("WOD 2"))
        .andExpect(jsonPath("$.content[1].movementRecords[?(@.name == 'Row')]").exists());
  }

  @Test
  @DisplayName("CREATE RECORD TEST")
  void createRecord() throws Exception {
    CreateMovementRecord movementRecord1 = CreateMovementRecord.builder().ord(1)
        .name("test record movement1").reps(10).ord(1)
        .build();
    CreateMovementRecord movementRecord2 = CreateMovementRecord.builder().ord(2)
        .name("test record movement2").reps(10).ord(2)
        .build();

    HashMap<String, Object> m = new HashMap<>() {
      {
        put("time", "14:00");
        put("reps", 50);
      }
    };

    CreateRecord record = CreateRecord.builder().date(LocalDate.now()).note("note 1")
        .wodId(Long.valueOf(1)).result(m)
        .movementRecords(List.of(movementRecord1, movementRecord2))
        .build();

    ResultActions result = this.mockMvc.perform(
        post("/api/v1/record").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(record))
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(
                Role.USER)));

    ResultActions expect = result.andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("user").exists())
        .andExpect(jsonPath("wod").exists())
        .andExpect(jsonPath("note").exists())
        .andExpect(jsonPath("date").exists())
        .andExpect(jsonPath("result").exists())
        ;
  }

  @Test
  @Transactional
  @DisplayName("UPDATE RECORD TEST")
  void updateRecord() throws Exception {
    Record record = recordService.getRecord(Long.valueOf(1));
    String changedNote = "changed note";
    String changedName = "changed name";
    String updateName = "update name1";

    List<MovementRecord> movementRecords = record.getMovementRecords().stream().map(
            mr -> MovementRecord.builder().id(mr.getId()).createdAt(mr.getCreatedAt()).updatedAt(mr.getUpdatedAt()).height(100)
                .distance(mr.getDistance())
                .cal(mr.getCal()).reps(mr.getReps()).name(changedName).build())
        .collect(Collectors.toList());

    movementRecords.add(
        MovementRecord.builder().name(updateName).ord(4).distance(1000).build());

    UpdateRecord updateRecord = UpdateRecord.builder().id(record.getId()).note(changedNote)
        .date(LocalDate.now())
        .movementRecords(movementRecords).wodId(Long.valueOf(3)).build();

    ResultActions result = this.mockMvc.perform(
        put("/api/v1/record/1").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRecord))
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN)));

    ResultActions expect = result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("note").value(changedNote))
        ;
  }

  @Test
  @Transactional
  @DisplayName("NOFOUND RECORD TEST")
  void notFoundUpdateRecord() throws Exception {
    Record record = recordService.getRecord(Long.valueOf(1));
    String changedNote = "changed note";
    String changedName = "changed name";
    String updateName = "update name1";

    List<MovementRecord> movementRecords = record.getMovementRecords().stream().map(
            mr -> MovementRecord.builder().id(mr.getId()).createdAt(mr.getCreatedAt()).updatedAt(mr.getUpdatedAt()).height(100)
                .distance(mr.getDistance())
                .cal(mr.getCal()).reps(mr.getReps()).name(changedName).build())
        .collect(Collectors.toList());

    movementRecords.add(
        MovementRecord.builder().name(updateName).ord(4).distance(1000).build());

    UpdateRecord updateRecord = UpdateRecord.builder().id(record.getId()).note(changedNote)
        .date(LocalDate.now())
        .movementRecords(movementRecords).wodId(Long.valueOf(3)).build();

    updateRecord.setId(Long.valueOf(10000));
    ResultActions result = this.mockMvc.perform(
        put("/api/v1/record/10000").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRecord))
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN)));

    ResultActions expect = result.andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("timestamp").exists())
        .andExpect(jsonPath("status").value(ErrorCode.RECORD_NOT_FOUND.getHttpStatus().value()))
        .andExpect(jsonPath("error").value(ErrorCode.RECORD_NOT_FOUND.getHttpStatus().name()))
        .andExpect(jsonPath("code").value(ErrorCode.RECORD_NOT_FOUND.getErrorCode()))
        .andExpect(jsonPath("message").value(""))
        .andExpect(jsonPath("details").value(ErrorCode.RECORD_NOT_FOUND.getDetail()))
        ;
  }
}