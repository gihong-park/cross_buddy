package com.kihong.health.persistence.service.user;

import com.kihong.health.persistence.dto.user.SignUpRequest;
import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.model.User.Role;
import com.kihong.health.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public User createUser(SignUpRequest signUpRequest) {
    String email = signUpRequest.getEmail();
    String username = signUpRequest.getUsername();
    String password = passwordEncoder.encode(signUpRequest.getPassword());
    Role role = signUpRequest.getRole() == null ? Role.USER : signUpRequest.getRole();

    User user = User.builder()
        .username(username)
        .password(password)
        .email(email)
        .role(role)
        .build();

    User createdUser = userRepository.save(user);

    return createdUser;
  }
}
