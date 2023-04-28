package com.kihong.health.persistence.dto.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kihong.health.persistence.model.Gender;
import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.model.User.Role;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserResponseTest {

  String email = "user@example.com";
  String username = "username";
  Gender gender = Gender.Male;
  LocalDate birthday = LocalDate.now();
  Role role = Role.USER;

  User user = User.builder().id(Long.valueOf(1)).email(email).username(username).birthDay(birthday)
      .gender(gender).role(role).build();

  @Test
  @DisplayName("GET VALUE FROM USER TEST")
  void getValueFromTest() {
    UserResponse userResponse = UserResponse.getValueFrom(user);

    assertEquals(user.getId(), userResponse.getId());
    assertEquals(user.getEmail(), userResponse.getEmail());
    assertEquals(user.getUsername(), userResponse.getUsername());
    assertEquals(user.getBirthDay(), userResponse.getBirthDay());
    assertEquals(user.getGender(), userResponse.getGender());
  }

}