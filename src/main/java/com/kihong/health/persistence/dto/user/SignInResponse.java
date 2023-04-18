package com.kihong.health.persistence.dto.user;

import com.kihong.health.persistence.model.User;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SignInResponse extends UserResponse {

  String token;

  static public SignInResponse getValueFrom(User user, String token) {
    return SignInResponse.builder().id(user.getId()).email(user.getEmail())
        .username(user.getUsername()).gender(user.getGender()).birthDay(user.getBirthDay())
        .token(token).build();
  }
}
