package com.kihong.health.web.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import com.kihong.health.persistence.dto.workoutOftheDay.CreateWorkoutOftheDay;
import com.kihong.health.persistence.model.User.Role;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
class WorkoutOftheDayControllerTest extends BaseControllerTest {

  @Test
  @DisplayName("GET LIST WOD BETWEEN DATE")
  void listWOD() throws Exception {
    ResultActions result = this.mockMvc.perform(get("/api/v1/wod").param("date", "2023-04-08"));

    ResultActions expect = result.andDo(print()).andExpect(status().isOk());
  }

  @Test
  @DisplayName("CREATE WOD TEST")
  void createWOD() throws Exception {
    CreateMovementRecord cmr1 = CreateMovementRecord.builder().name("create movement record")
        .weight(135).reps(10).ord(1).build();
    CreateMovementRecord cmr2 = CreateMovementRecord.builder().name("create movement record")
        .height(10).reps(20).ord(2).build();

    HashMap<String, Object> m = new HashMap<>() {{
      put("time", "00:00");
      put("reps", 0);
    }};
    String name = "create name", type = "create type", description = "create description";

    List<CreateMovementRecord> createMovementRecordList = List.of(cmr1, cmr2);
    CreateWorkoutOftheDay createWorkoutOftheDay = CreateWorkoutOftheDay.builder().name(name)
        .type(type).description(description).date(
            LocalDate.now()).cmrList(createMovementRecordList).result(m).build();

    ResultActions result = this.mockMvc.perform(post("/api/v1/wod").contentType(
            MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(createWorkoutOftheDay))
        .header(
            HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN)));

    ResultActions expect = result.andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("name").value(name))
        .andExpect(jsonPath("type").value(type))
        .andExpect(jsonPath("description").value(description))
        .andExpect(jsonPath("date").exists())
        .andExpect(jsonPath("result").isNotEmpty());
  }
}