package com.kihong.health.web.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.common.Documentation;
import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import com.kihong.health.persistence.dto.workoutOftheDay.CreateWorkoutOftheDay;
import com.kihong.health.persistence.model.User.Role;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
class WorkoutOftheDayControllerTest extends BaseControllerTest {

  private FieldDescriptor[] wodFields(FieldDescriptor... fieldDescriptors) {
    var fields = new FieldDescriptor[]{
        fieldWithPath("name").description("이름"),
        fieldWithPath("type").description("와드 타입"),
        fieldWithPath("description").description("와드 내용"),
        fieldWithPath("date").description("와드 날짜"),
        fieldWithPath("result").description("와드 결과")
    };

    return Documentation.concatWithStream(fields, fieldDescriptors);
  }

  private FieldDescriptor[] wodResponseFields(FieldDescriptor... fieldDescriptors) {
    var fields = wodFields(
        fieldWithPath("id").description("id"),
        fieldWithPath("movement_records[]").description("기록해야할 동작들")
    );

    return Documentation.concatWithStream(fields, fieldDescriptors);
  }

  @Test
  @DisplayName("GET LIST WOD BETWEEN DATE")
  void listWOD() throws Exception {
    ResultActions result = this.mockMvc.perform(get("/api/v1/wod").param("date", "2023-04-08"));

    ResultActions expect = result.andDo(print()).andExpect(status().isOk());

    expect.andDo(document("resources-wod-list",
        links(
            Documentation.defaultLinks()
        ),
        queryParameters(
            parameterWithName("date").description("주의 기준이 되는 날짜")
        ),
        responseHeaders(Documentation.responseHeaderFields()),
        relaxedResponseFields(
            Documentation.pageFields(Documentation.linkFields(
                fieldWithPath("_embedded.wods[]").description("한주의 와드 리스트")
            ))
        ).andWithPrefix("_embedded.wods[].",
            Documentation.linkFields(
                fieldWithPath("id").description("id"),
                fieldWithPath("name").description("와드 이름"),
                fieldWithPath("type").description("와드 타입"),
                fieldWithPath("description").description("와드 내용"),
                fieldWithPath("date").description("와드 날짜"),
                fieldWithPath("result").description("결과 입력"),
                fieldWithPath("movement_records[]").description("기록해야할 동작들")
            )
        )
    ));
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
        .accept(MediaTypes.HAL_JSON)
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

    expect.andDo(document("resources-wod-create",
        links(
            Documentation.defaultLinks()
        ),
        requestHeaders(
            Documentation.requestHeaderFields(Documentation.authorizationHeaderField())
        ),
        relaxedRequestFields(
            wodFields(
                fieldWithPath("cmrList").description("동작별 기록 생성")
            )
        ),
        responseHeaders(Documentation.responseHeaderFields()),
        relaxedResponseFields(
            Documentation.linkFields(
                wodResponseFields()
            )
        )
    ));
  }
}