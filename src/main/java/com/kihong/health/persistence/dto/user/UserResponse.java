package com.kihong.health.persistence.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kihong.health.persistence.model.User;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.hateoas.server.core.Relation;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "users", itemRelation = "user")
public class UserResponse {

  Long id;
  String email;
  String username;
  String gender;
  @JsonProperty("birth_day")
  LocalDate birthDay;

  static public UserResponse getValueFrom(User user) {
    return UserResponse.builder().id(user.getId()).email(user.getEmail())
        .username(user.getUsername()).gender(user.getGender()).birthDay(user.getBirthDay()).build();
  }
}
