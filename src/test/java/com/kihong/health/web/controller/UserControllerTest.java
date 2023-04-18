package com.kihong.health.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kihong.health.common.BaseControllerTest;
import com.kihong.health.persistence.dto.user.SignInDTO;
import com.kihong.health.persistence.dto.user.SignUpDTO;
import com.kihong.health.persistence.model.User.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends BaseControllerTest {

  @Test
  @DisplayName("SIGN IN USER TEST")
  void signIn() throws Exception{
    SignUpDTO signUpDTO = getUserByRole(Role.USER);

    SignInDTO signInDTO = SignInDTO.builder()
        .usernameOremail(signUpDTO.getEmail())
        .password(signUpDTO.getPassword())
        .build();
    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInDTO)));

    perform.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").exists())
        .andExpect(jsonPath("email").exists())
        .andExpect(jsonPath("username").exists())
        .andExpect(jsonPath("token").exists())
    ;
  }
}