package com.kihong.health.persistence.dto.user;

import com.kihong.health.persistence.model.Gender;
import com.kihong.health.persistence.model.User.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
  @NotEmpty
  private String username;
  @NotEmpty
  @Email
  private String email;
  @NotEmpty
  private String password;
  @NotEmpty
  private Role role;

}