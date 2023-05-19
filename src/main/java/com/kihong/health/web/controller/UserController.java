package com.kihong.health.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kihong.health.persistence.dto.user.RefreshRequest;
import com.kihong.health.persistence.dto.user.SignInRequest;
import com.kihong.health.persistence.dto.user.SignInResponse;
import com.kihong.health.persistence.dto.user.SignUpRequest;
import com.kihong.health.persistence.dto.user.TokenInfo;
import com.kihong.health.persistence.dto.user.UserResponse;
import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.repository.UserRepository;
import com.kihong.health.persistence.service.user.UserService;
import com.kihong.health.web.exception.ErrorCode;
import com.kihong.health.web.exception.HttpException;
import com.kihong.health.web.secure.JwtTokenProvider;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;
  private final long expired = 86400000;


  @PostMapping("/signup")
  public ResponseEntity
  create(@RequestBody @Valid SignUpRequest signUpRequest, Errors errors) throws JsonProcessingException {
    if (errors.hasErrors()) {
      throw new HttpException(ErrorCode.BAD_REQUEST,
          errors.toString());
    }
    if (userRepository.findByEmail(signUpRequest.getEmail())
        .isPresent()) {
      log.error("이미 존재하는 이메일입니다.");
      throw new HttpException(ErrorCode.CONFLICT , null);
    }
    User user = userService.createUser(signUpRequest);

    return ResponseEntity.status(HttpStatus.CREATED)
        .location(URI.create("/api/v1/user/" + user.getId()))
        .body(EntityModel.of(UserResponse.getValueFrom(user)));
  }

  @PostMapping("/signin")
  public ResponseEntity SignIn(@RequestBody @Valid SignInRequest signIn, Errors errors) {
    if (errors.hasErrors()) {
      log.error(String.format("잘못된 요청: %s", errors));
      throw new HttpException(ErrorCode.BAD_REQUEST, errors.toString());
    }

    Optional<User> getUser = userRepository.findByEmail(signIn.getUsernameOremail());
    if (getUser.isEmpty()) {
      getUser = userRepository.findByUsername(signIn.getUsernameOremail());
    }
    if (getUser.isEmpty()) {
      log.error("존재하지않은 유저입니다.");
      throw new HttpException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 유저입니다.");
    }
    if (!passwordEncoder.matches(signIn.getPassword(), getUser.get().getPassword())) {
      log.error("비밀번호가 틀렸습니다.");
      throw new HttpException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 유저입니다.");
    }

    TokenInfo tokenInfo = jwtTokenProvider.createToken(getUser.get().getEmail(),
        getUser.get().getAuthorities(), this.expired);
    return ResponseEntity.ok(EntityModel.of(SignInResponse.getValueFrom(getUser.get(), tokenInfo)));

  }

  @PostMapping("/refresh")
  public ResponseEntity RefreshToken(@RequestBody @Valid RefreshRequest refreshRequest,
      Errors errors) {
    if (errors.hasErrors()) {
      log.error(String.format("잘못된 요청: %s", errors));
      throw new HttpException(ErrorCode.BAD_REQUEST, errors.toString());
    }
    if (!jwtTokenProvider.validateToken(refreshRequest.getRefreshToken())) {
      log.error("잘못된 토큰입니다.");
      throw new HttpException(ErrorCode.BAD_REQUEST, "잘못된 토큰입니다.");
    }
    String email = jwtTokenProvider.getUserPk(refreshRequest.getRefreshToken());
    Optional<User> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      log.error("유저가 존재하지 않습니다.");
      throw new HttpException(ErrorCode.USER_NOT_FOUND, "존재하지 않는 유저입니다.");
    }

    TokenInfo tokenInfo = jwtTokenProvider.createToken(user.get().getEmail(),
        user.get().getAuthorities(), this.expired);

    return ResponseEntity.ok(EntityModel.of(tokenInfo));
  }
}
