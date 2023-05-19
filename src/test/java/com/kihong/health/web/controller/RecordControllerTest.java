package com.kihong.health.web.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.common.Documentation;
import com.kihong.health.persistence.dto.movementRecord.CreateMovementRecord;
import com.kihong.health.persistence.dto.record.CreateRecord;
import com.kihong.health.persistence.dto.record.UpdateRecord;
import com.kihong.health.persistence.model.MovementRecord;
import com.kihong.health.persistence.model.Record;
import com.kihong.health.persistence.model.User.Role;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.ResultActions;

class RecordControllerTest extends BaseControllerTest {

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  RecordService recordService;

  private FieldDescriptor[] recordFields(FieldDescriptor... fieldDescriptors) {
    var recordFields = new FieldDescriptor[]{
        fieldWithPath("date").description("시행 날짜"),
        fieldWithPath("description").description("상세 기록"),
        fieldWithPath("note").description("노트"),
        fieldWithPath("movement_records[]").description("동작별 기록"),
        fieldWithPath("result").description("결과")
    };
    return Documentation.concatWithStream(recordFields, fieldDescriptors);
  }

  private FieldDescriptor[] recordResponseFields(FieldDescriptor... fieldDescriptors) {
    return Documentation.concatWithStream(recordFields(
        fieldWithPath("id").description("id"),
        fieldWithPath("user").description("기록한 유저"),
        fieldWithPath("wod").description("와드 내용"),
        fieldWithPath("_links.self.href").description("셀프 링크"),
        fieldWithPath("_links.profile.href").description("다큐먼트 링크")
    ), fieldDescriptors);
  }

  @Test
  @DisplayName("LIST RECORD TEST")
  void listRecord() throws Exception {
    ResultActions result = this.mockMvc.perform(
        get("/api/v1/record").header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN))
            .param("size", "1")
            .param("page", "0")
            .param("sort", "id," +
                "asc")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON));

    ResultActions expect = result.andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.records[0].id").value(1))
        .andExpect(jsonPath("$._embedded.records[0].wod.name").value("WOD 1"))
        .andExpect(jsonPath("$._embedded.records[0].result").exists())
        .andExpect(jsonPath(
            "$._embedded.records[0].movement_records[?(@.name == 'Row')]").exists());

    expect.andDo(document("resources-record-list",
        links(
            Documentation.listLinks()
        ),
        requestHeaders(
            Documentation.requestHeaderFields()
        ),
        queryParameters(
            Documentation.pageParameter()
        ),
        responseHeaders(
            Documentation.responseHeaderFields()
        ),
        relaxedResponseFields(
            Documentation.pageLinkFields(
                fieldWithPath("_embedded.records[]").description("기록 리스트")
            )
        )
            .andWithPrefix(
                "_embedded.records[].",
                recordResponseFields()
            )
    ));
  }

  @Test
  @DisplayName("FORBIDDEN LIST RECORD")
  void forbiddenListRecord() throws Exception {
    ResultActions result = this.mockMvc.perform(
        get("/api/v1/record").param("id", "1")
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.USER)));

    ResultActions expect = result.andDo(print()).andExpect(status().isForbidden())
        .andExpect(jsonPath("timestamp").exists())
        .andExpect(jsonPath("status").value(ErrorCode.USER_FORBIDDEN.getHttpStatus().value()))
        .andExpect(jsonPath("error").value(ErrorCode.USER_FORBIDDEN.getHttpStatus().name()))
        .andExpect(jsonPath("code").value(ErrorCode.USER_FORBIDDEN.getErrorCode()))
        .andExpect(jsonPath("message").value("User Forbidden"))
        .andExpect(jsonPath("details").value(ErrorCode.USER_FORBIDDEN.getDetail()));
  }

  @Test
  @DisplayName("SEARCH RECORD TEST")
  void searchRecord() throws Exception {
    ResultActions result = this.mockMvc.perform(
        get("/api/v1/record").header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN))
            .param("search", "row"));

    ResultActions expect = result.andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("$._embedded.records[0].id").value(1))
        .andExpect(jsonPath("$._embedded.records[0].wod.name").value("WOD 1"))
        .andExpect(jsonPath("$._embedded.records[0].result").exists())
        .andExpect(jsonPath(
            "$._embedded.records[0].movement_records[?(@.name == 'Row')]").exists());
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
            .accept(MediaTypes.HAL_JSON)
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
        .andExpect(jsonPath("result").exists());

    expect.andDo(document("resources-record-create",
        links(
            Documentation.defaultLinks()
        ),
        requestHeaders(
            Documentation.requestHeaderFields(Documentation.authorizationHeaderField())
        ),
        relaxedRequestFields(
            recordFields(
                fieldWithPath("wod_id").description("와드 id")
            )
        ),
        responseHeaders(
            Documentation.responseHeaderFields()
        ),
        relaxedResponseFields(
            recordResponseFields()
        )

    ));
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
            mr -> MovementRecord.builder().id(mr.getId()).createdAt(mr.getCreatedAt())
                .updatedAt(mr.getUpdatedAt()).height(100)
                .distance(mr.getDistance())
                .cal(mr.getCal()).reps(mr.getReps()).name(changedName).build())
        .collect(Collectors.toList());

    movementRecords.add(
        MovementRecord.builder().name(updateName).ord(4).distance(1000).build());

    UpdateRecord updateRecord = UpdateRecord.builder().id(record.getId()).note(changedNote)
        .date(LocalDate.now())
        .movementRecords(movementRecords).wodId(Long.valueOf(3)).build();

    ResultActions result = this.mockMvc.perform(
        put("/api/v1/record/{id}", updateRecord.getId()).contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
            .content(objectMapper.writeValueAsString(updateRecord))
            .header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN)));

    ResultActions expect = result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("note").value(changedNote));

    expect.andDo(document("resources-record-put",
        links(
            Documentation.defaultLinks()
        ),
        pathParameters(
            parameterWithName("id").description("기록 id")
        ),
        relaxedRequestFields(
            recordFields(
                fieldWithPath("id").description("id"),
                fieldWithPath("wod_id").description("와드 id")
            )
        ),
        requestHeaders(
            Documentation.requestHeaderFields(Documentation.authorizationHeaderField())
        ),
        responseHeaders(
            Documentation.responseHeaderFields()
        ),
        relaxedResponseFields(
            recordResponseFields()
        )
    ));
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
            mr -> MovementRecord.builder().id(mr.getId()).createdAt(mr.getCreatedAt())
                .updatedAt(mr.getUpdatedAt()).height(100)
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
        .andExpect(jsonPath("details").value(ErrorCode.RECORD_NOT_FOUND.getDetail()));
  }

  @Test
  @DisplayName("GET RECORD")
  void getRecord() throws Exception {

    ResultActions result = this.mockMvc.perform(
        get("/api/v1/record/{id}", 1).header(HttpHeaders.AUTHORIZATION, getAccessToken(Role.ADMIN)));

    ResultActions expect = result.andDo(print()).andExpect(status().isOk());

    expect.andDo(document("resources-record-get",
        links(
            Documentation.defaultLinks()
        ),
        requestHeaders(Documentation.authorizationHeaderField()),
        pathParameters(
            parameterWithName("id").description("record id")
        ),
        responseHeaders(
            Documentation.responseHeaderFields()
        ),
        relaxedResponseFields(
            recordResponseFields()
        )
    ));
  }
}
