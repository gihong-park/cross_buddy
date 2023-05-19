package com.kihong.health.persistence.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshRequest {

  @NotEmpty
  @JsonProperty("refresh_token")
  private String refreshToken;
  @NotNull
  private Long userId;
}
