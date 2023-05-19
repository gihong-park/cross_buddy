package com.kihong.health.common;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;

public class Documentation {

  public static HeaderDescriptor[] requestHeaderFields(HeaderDescriptor... headerDescriptors) {
    var headers = new HeaderDescriptor[]{
        headerWithName(HttpHeaders.ACCEPT).description("accept header"),
        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
    };

    return concatWithStream(headers, headerDescriptors);
  }

  public static HeaderDescriptor[] responseHeaderFields(HeaderDescriptor... headerDescriptors) {
    var headers = new HeaderDescriptor[]{
        headerWithName(HttpHeaders.CONTENT_TYPE).description("JSON+HAL")
    };

    return concatWithStream(headers, headerDescriptors);
  }

  public static HeaderDescriptor authorizationHeaderField() {
    return headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰 기입");
  }

  public static LinkDescriptor[] defaultLinks(LinkDescriptor... linkDescriptors) {
    LinkDescriptor[] links = new LinkDescriptor[]{
        linkWithRel("self").description("셀프 링크"),
        linkWithRel("profile").description("다큐먼트 링크"),
    };

    return concatWithStream(links, linkDescriptors);
  }

  public static LinkDescriptor[] listLinks(LinkDescriptor... linkDescriptors) {
    var links = defaultLinks(
        linkWithRel("next").description("다음 페이지 링크"),
        linkWithRel("last").description("마지막 페이지 링크"),
        linkWithRel("first").description("첫번째 페이지 링크")
    );

    return concatWithStream(links, linkDescriptors);
  }

  public static ParameterDescriptor[] pageParameter(ParameterDescriptor... parameterDescriptors) {
    ParameterDescriptor[] params = new ParameterDescriptor[]{
        parameterWithName("page").description("현재 페이지"),
        parameterWithName("size").description("페이지 당 데이터 갯수"),
        parameterWithName("sort").description("정렬 기준 및 순서")
    };

    return concatWithStream(params, parameterDescriptors);
  }

  public static FieldDescriptor[] pageFields(FieldDescriptor... responseFields) {
    var pageFields = new FieldDescriptor[]{
        fieldWithPath("page.size").description("페이지 당 갯수"),
        fieldWithPath("page.totalElements").description("총 갯수"),
        fieldWithPath("page.totalPages").description("총 페이지"),
        fieldWithPath("page.number").description("현재 페이지"),
    };

    return concatWithStream(pageFields, responseFields);

  }

  public static FieldDescriptor[] linkFields(FieldDescriptor... responseFields) {
    var linkFields = new FieldDescriptor[]{
        fieldWithPath("_links.self.href").description("셀프 링크"),
        fieldWithPath("_links.profile.href").description("다큐먼트 링크"),
    };

    return concatWithStream(linkFields, responseFields);
  }

  public static FieldDescriptor[] pageLinkFields(FieldDescriptor... responseFields) {
    var pageFields = pageFields(
        linkFields(
            fieldWithPath("_links.first.href").description("첫 페이지 링크"),
            fieldWithPath("_links.next.href").description("다음 페이지 링크"),
            fieldWithPath("_links.last.href").description("마지막 페이지 링크")
        )
    );

    return concatWithStream(pageFields, responseFields);
  }

  public static <T> T[] concatWithStream(T[] array1, T[] array2) {
    return Stream.concat(Arrays.stream(array1), Arrays.stream(array2))
        .toArray(size -> (T[]) Array.newInstance(array1.getClass().getComponentType(), size));
  }
}
