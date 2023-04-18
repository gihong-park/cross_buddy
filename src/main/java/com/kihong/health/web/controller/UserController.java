package com.kihong.health.web.controller;

import com.kihong.health.persistence.dto.user.SignInDTO;
import com.kihong.health.persistence.dto.user.SignInResponse;
import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.repository.UserRepository;
import com.kihong.health.web.exception.ErrorCode;
import com.kihong.health.web.exception.HttpException;
import com.kihong.health.web.secure.JwtTokenProvider;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
public class UserController {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;

  @PostMapping("/signin")
  public ResponseEntity SignIn(@RequestBody @Valid SignInDTO signIn, Errors errors) {
    if(errors.hasErrors()) {
      throw new HttpException(ErrorCode.BAD_REQUEST ,errors.toString());
    }

    Optional<User> getUser = userRepository.findByEmail(signIn.getUsernameOremail());
    if (getUser.isEmpty()) {
      getUser = userRepository.findByUsername(signIn.getUsernameOremail());
    }
    if(getUser.isEmpty()) {
      throw new HttpException(ErrorCode.USER_NOT_FOUND,  "User doesn't exist");
    }
    if(!passwordEncoder.matches(signIn.getPassword(), getUser.get().getPassword())) {
      throw new HttpException(ErrorCode.USER_NOT_FOUND, "User doesn't exist");
    }

    String token = jwtTokenProvider.createToken(getUser.get().getEmail(), getUser.get().getAuthorities());
    return ResponseEntity.ok(SignInResponse.getValueFrom(getUser.get(), token));

  }
}
