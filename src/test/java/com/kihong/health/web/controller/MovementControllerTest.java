package com.kihong.health.web.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kihong.health.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
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
                linkWithRel("self").description("리스트 링크"),
                linkWithRel("profile").description("다큐먼트 링크"),
                linkWithRel("next").description("다음 페이지 링크"),
                linkWithRel("last").description("마지막 페이지 링크"),
                linkWithRel("first").description("첫번째 페이지 링크")
            ),
            queryParameters(
                parameterWithName("page").description("현재 페이지"),
                parameterWithName("size").description("페이지 당 데이터 갯수"),
                parameterWithName("sort").description("정렬 기준 및 순서")
            ),
            responseHeaders(
                headerWithName(HttpHeaders.CONTENT_TYPE).description("JSON+HAL")
            ),
            responseFields(
                fieldWithPath("_embedded.movements[]").description("동작 리스트"),
                fieldWithPath("page.size").description("페이지 당 갯수"),
                fieldWithPath("page.totalElements").description("총 갯수"),
                fieldWithPath("page.totalPages").description("총 페이지"),
                fieldWithPath("page.number").description("현재 페이지"),
                fieldWithPath("_links.self.href").description("셀프 링크"),
                fieldWithPath("_links.profile.href").description("다큐먼트 링크"),
                fieldWithPath("_links.first.href").description("첫 페이지 링크"),
                fieldWithPath("_links.next.href").description("다음 페이지 링크"),
                fieldWithPath("_links.last.href").description("마지막 페이지 링크")
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