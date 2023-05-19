package com.kihong.health.web.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.common.Documentation;
import com.kihong.health.persistence.dto.user.RefreshRequest;
import com.kihong.health.persistence.dto.user.SignInRequest;
import com.kihong.health.persistence.dto.user.SignUpRequest;
import com.kihong.health.persistence.dto.user.TokenInfo;
import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.model.User.Role;
import com.kihong.health.persistence.repository.UserRepository;
import com.kihong.health.web.exception.ErrorCode;
import com.kihong.health.web.secure.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends BaseControllerTest {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("SIGN UP USER TEST")
  void signUp() throws Exception {
    SignUpRequest signUpRequest = SignUpRequest.builder().username("test user")
        .email("test@example.com").password("password").build();

    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaTypes.HAL_JSON)
        .content(objectMapper.writeValueAsString(signUpRequest))
    );

    ResultActions expect = perform.andDo(print())
        .andExpect(status().isCreated());

    expect.andDo(document("user-signup",
        requestHeaders(
            headerWithName(HttpHeaders.ACCEPT).description("accept header"),
            headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
        ),
        requestFields(
            fieldWithPath("username").description("유저의 이름"),
            fieldWithPath("email").description("이메일"),
            fieldWithPath("password").description("비밀번호"),
            fieldWithPath("role").description("유저 역할")
        ),
        responseHeaders(
            headerWithName(HttpHeaders.CONTENT_TYPE).description("JSON + HAL")
        ),
        responseFields(
            fieldWithPath("id").description("유저 ID"),
            fieldWithPath("username").description("유저의 이름"),
            fieldWithPath("email").description("이메일"),
            fieldWithPath("gender").description("성별"),
            fieldWithPath("birth_day").description("생년월일")
        )
    ))
    ;
  }

  @Test
  @DisplayName("SIGN IN USER TEST")
  void signIn() throws Exception {
    SignUpRequest signUpRequest = getUserByRole(Role.USER);

    SignInRequest signInRequest = SignInRequest.builder()
        .usernameOremail(signUpRequest.getEmail())
        .password(signUpRequest.getPassword())
        .build();
    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaTypes.HAL_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)));

    ResultActions expect = perform.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("email").exists())
        .andExpect(jsonPath("username").exists())
        .andExpect(jsonPath("tokenInfo").exists());

    expect.andDo(document("user-signin",
        requestHeaders(
            Documentation.requestHeaderFields()
        ),
        requestFields(
            fieldWithPath("usernameOremail").description("유저이름 혹은 이메일을 통한 로그인"),
            fieldWithPath("password").description("비밀번호")
        ),
        responseHeaders(
          Documentation.responseHeaderFields()
        ),
        responseFields(
            fieldWithPath("id").description("유저를 구분 짓는 unique ID"),
            fieldWithPath("email").description("이메일"),
            fieldWithPath("username").description("이름"),
            fieldWithPath("gender").description("성별"),
            fieldWithPath("birth_day").description("생년월일"),
            fieldWithPath("tokenInfo.grant_type").description("허가 타입"),
            fieldWithPath("tokenInfo.access_token").description("로그인에 필요한 토큰"),
            fieldWithPath("tokenInfo.refresh_token").description("access token을 연장할 수 있는 토큰")
        )
    ));
  }

  @Test
  @DisplayName("REFRESH TOKEN TEST")
  void refresh() throws Exception {
    long expired = 86400000;

    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaTypes.HAL_JSON)
        .content(objectMapper.writeValueAsString(getRefreshRequest(expired)))
    );

    ResultActions expect = perform.andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("refresh_token").exists()).andExpect(jsonPath("access_token").exists());

  }

  @Test
  @DisplayName("REFRESH TOKEN EXPIRED TEST")
  void refreshExpired() throws Exception {
    long expired = -100;

    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(getRefreshRequest(expired)))
    );

    ResultActions expect = perform.andDo(print()).andExpect(status().isUnauthorized())
        .andExpect(jsonPath("code").value(
            ErrorCode.TOKEN_EXPIRED.getErrorCode()))
        .andExpect(jsonPath("details").value(ErrorCode.TOKEN_EXPIRED.getDetail()));

  }

  @Test
  @DisplayName("REFRESH TOKEN INVALID TEST")
  void refreshInvalid() throws Exception {
    long expired = 86400000;

    RefreshRequest refreshRequest = getRefreshRequest(expired);
    refreshRequest.setRefreshToken(refreshRequest.getRefreshToken().substring(5));
    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(refreshRequest))
    );

    ResultActions expect = perform.andDo(print()).andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(
            ErrorCode.BAD_REQUEST.getErrorCode()))
        .andExpect(jsonPath("details").value(ErrorCode.BAD_REQUEST.getDetail()));

  }

  @Test
  @DisplayName("REFRESH TOKEN REQUEST INVALID TEST")
  void refreshRequestInvalid() throws Exception {
    long expired = 86400000;

    RefreshRequest refreshRequest = getRefreshRequest(expired);
    refreshRequest.setRefreshToken(null);
    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(refreshRequest))
    );

    ResultActions expect = perform.andDo(print()).andExpect(status().isBadRequest())
        .andExpect(jsonPath("code").value(
            ErrorCode.BAD_REQUEST.getErrorCode()))
        .andExpect(jsonPath("details").value(ErrorCode.BAD_REQUEST.getDetail()));
  }

  RefreshRequest getRefreshRequest(long expired) {
    SignUpRequest user = this.getUserByRole(Role.USER);

    Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

    TokenInfo tokenInfo = jwtTokenProvider.createToken(optionalUser.get().getEmail(),
        optionalUser.get().getAuthorities(), expired);
    RefreshRequest refreshRequest = RefreshRequest.builder()
        .refreshToken(tokenInfo.getRefreshToken()).userId(optionalUser.get().getId()).build();

    return refreshRequest;
  }
}
