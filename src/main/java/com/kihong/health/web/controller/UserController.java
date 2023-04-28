package com.kihong.health.web.controller;

import com.kihong.health.persistence.dto.user.RefreshRequest;
import com.kihong.health.persistence.dto.user.SignInDTO;
import com.kihong.health.persistence.dto.user.SignInResponse;
import com.kihong.health.persistence.dto.user.TokenInfo;
import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.repository.UserRepository;
import com.kihong.health.web.exception.ErrorCode;
import com.kihong.health.web.exception.HttpException;
import com.kihong.health.web.secure.JwtTokenProvider;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final long expired = 86400000;


  @PostMapping("/signin")
  public ResponseEntity SignIn(@RequestBody @Valid SignInDTO signIn, Errors errors) {
    if (errors.hasErrors()) {
      throw new HttpException(ErrorCode.BAD_REQUEST, errors.toString());
    }

    Optional<User> getUser = userRepository.findByEmail(signIn.getUsernameOremail());
    if (getUser.isEmpty()) {
      getUser = userRepository.findByUsername(signIn.getUsernameOremail());
    }
    if (getUser.isEmpty()) {
      throw new HttpException(ErrorCode.USER_NOT_FOUND, "User doesn't exist");
    }
    if (!passwordEncoder.matches(signIn.getPassword(), getUser.get().getPassword())) {
      throw new HttpException(ErrorCode.USER_NOT_FOUND, "User doesn't exist");
    }

    TokenInfo tokenInfo = jwtTokenProvider.createToken(getUser.get().getEmail(),
        getUser.get().getAuthorities(), this.expired);
    return ResponseEntity.ok(SignInResponse.getValueFrom(getUser.get(), tokenInfo));

  }

  @PostMapping("/refresh")
  public ResponseEntity RefreshToken(@RequestBody @Valid RefreshRequest refreshRequest,
      Errors errors) {
    if (errors.hasErrors()) {
      throw new HttpException(ErrorCode.BAD_REQUEST, errors.toString());
    }
    if (!jwtTokenProvider.validateToken(refreshRequest.getRefreshToken())) {
      throw new HttpException(ErrorCode.BAD_REQUEST, "잘못된 토큰입니다.");
    }
    String email = jwtTokenProvider.getUserPk(refreshRequest.getRefreshToken());
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      throw new HttpException(ErrorCode.USER_NOT_FOUND, "User doesn't exist");
    }

    TokenInfo tokenInfo = jwtTokenProvider.createToken(user.get().getEmail(),
        user.get().getAuthorities(), this.expired);

    return ResponseEntity.ok(tokenInfo);
  }
}
