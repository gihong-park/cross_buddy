package com.kihong.health.web.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.kihong.health.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;

class MovementControllerTest extends BaseControllerTest {

  @Test
  @DisplayName("LIST MOVEMENT TEST")
  void listMovement() throws Exception {
    ResultActions result = this.mockMvc.perform(get("/api/v1/movement"));

    ResultActions expect = result.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("content").exists())
        .andExpect(jsonPath("content[0].id").exists())
        .andExpect(jsonPath("content[0].name").exists())
        .andExpect(jsonPath("content[0].description").exists())
        ;
  }
}