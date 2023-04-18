package com.kihong.health.persistence.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInDTO {
  @NotEmpty
  private String usernameOremail;
  @NotEmpty
  private String password;
}