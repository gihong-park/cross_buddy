package com.kihong.health.persistence.dto.user;

import com.kihong.health.persistence.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse extends UserResponse {

  TokenInfo tokenInfo;

  static public SignInResponse getValueFrom(User user, TokenInfo tokenInfo) {
    return SignInResponse.builder().id(user.getId()).email(user.getEmail())
        .username(user.getUsername()).gender(user.getGender()).birthDay(user.getBirthDay())
        .tokenInfo(tokenInfo).build();
  }
}
