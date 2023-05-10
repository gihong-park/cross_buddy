package com.kihong.health.web.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kihong.health.common.BaseControllerTest;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends BaseControllerTest {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private UserRepository userRepository;

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
        .content(objectMapper.writeValueAsString(signInRequest)));

    perform.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("email").exists())
        .andExpect(jsonPath("username").exists())
        .andExpect(jsonPath("tokenInfo").exists())
    ;
  }

  @Test
  @DisplayName("REFRESH TOKEN TEST")
  void refresh() throws Exception {
    long expired = 86400000;

    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(getRefreshRequest(expired)))
    );

    ResultActions expect = perform.andDo(print()).andExpect(status().isOk())
        .andExpect(jsonPath("refreshToken").exists()).andExpect(jsonPath("accessToken").exists());

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
