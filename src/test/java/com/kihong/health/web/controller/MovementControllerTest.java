package com.kihong.health.web.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.common.Documentation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

class MovementControllerTest extends BaseControllerTest {

  @Test
  @DisplayName("LIST MOVEMENT TEST")
  void listMovement() throws Exception {
    ResultActions result = this.mockMvc.perform(
        get("/api/v1/movement").param("size", "2").param("page", "0")
            .param("sort", "id,desc"));

    ResultActions expect = result.andDo(print())
        .andExpect(status().isOk());

    expect.andDo(document("resources-movement-list",
            links(
                Documentation.listLinks()
            ),
            queryParameters(
                Documentation.pageParameter()
            ),
            responseHeaders(
                Documentation.responseHeaderFields()
            ),
            responseFields(
                Documentation.pageLinkFields(
                    fieldWithPath("_embedded.movements[]").description("동작 리스트")
                )
            ).andWithPrefix(
                "_embedded.movements[].",
                fieldWithPath("name").description("동작 이름"),
                fieldWithPath("description").description("동작 설명"),
                fieldWithPath("_links.self.href").description("셀프 링크"),
                fieldWithPath("_links.profile.href").description("다큐먼트 링크")
            )

        )
    );
  }
}