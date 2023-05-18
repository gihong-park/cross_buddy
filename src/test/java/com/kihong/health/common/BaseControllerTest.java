package com.kihong.health.common;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kihong.health.persistence.dto.user.SignInRequest;
import com.kihong.health.persistence.dto.user.SignInResponse;
import com.kihong.health.persistence.dto.user.SignUpRequest;
import com.kihong.health.persistence.dto.user.TokenInfo;
import com.kihong.health.persistence.model.User.Role;
import com.kihong.health.persistence.service.user.UserService;
import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Sql({"/testdata/schema.sql", "/testdata/insertWOD.sql", "/testdata/insertUser.sql",
    "/testdata/insertRecord.sql", "/testdata/insertMovementRecord.sql",
    "/testdata/insertMovement.sql"})
@Import(com.kihong.health.common.RestDocsConfiguration.class)
public abstract class BaseControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected UserService userService;

  protected SignUpRequest normalUser = SignUpRequest.builder().email("normal_user@example.com")
      .username("normal_user").password("password1").role(Role.USER).build();
  protected SignUpRequest adminUser = SignUpRequest.builder().email("admin_user@example.com")
      .username("admin_user").password("password2").role(Role.ADMIN).build();
  protected SignUpRequest masterUser = SignUpRequest.builder().email("master_user@example.com")
      .username("master_user").password("password3").role(Role.MASTER).build();
  @Autowired
  protected ObjectMapper objectMapper;

  @BeforeEach
  void beforeEach() {
    userService.createUser(normalUser);
    userService.createUser(adminUser);
    userService.createUser(masterUser);
  }

  protected String getAccessToken(Role role) throws Exception {
    SignUpRequest signUpRequest = getUserByRole(role);
    SignInRequest signInRequest = SignInRequest.builder()
        .usernameOremail(signUpRequest.getEmail())
        .password(signUpRequest.getPassword())
        .build();
    ResultActions perform = this.mockMvc.perform(post("/api/v1/user/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signInRequest)));

    var responseBody = perform.andReturn()
        .getResponse()
        .getContentAsString();

    TokenInfo tokenInfo = objectMapper.convertValue(objectMapper.readValue(responseBody, Map.class).get("tokenInfo"), TokenInfo.class);

    return "Bearer " + tokenInfo.getAccessToken();
  }

  protected SignUpRequest getUserByRole(Role role) {
    if (role == Role.USER) {
      return normalUser;
    } else if (role == Role.ADMIN) {
      return adminUser;
    } else {
      return masterUser;
    }
  }

  static final DockerComposeContainer composeContainer;

  static {
    composeContainer = new DockerComposeContainer(
        new File("src/test/resources/docker-compose.yml")).withExposedService("test-mysql", 3306);
  }

  @DynamicPropertySource
  static void mysqlProperties(DynamicPropertyRegistry registry) {
    composeContainer.start();
    registry.add("spring.datasource.url", () -> String.format("jdbc:mysql://%s:%s/test_db",
        composeContainer.getServiceHost("test-mysql", 3306),
        composeContainer.getServicePort("test-mysql", 3306)));
  }

}
