package com.kihong.health.persistence.service.user;

import com.kihong.health.persistence.dto.user.SignUpDTO;
import com.kihong.health.persistence.model.User;
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
  public User createUser(SignUpDTO signUpDTO) {
    String email = signUpDTO.getEmail();
    String username = signUpDTO.getUsername();
    String password = passwordEncoder.encode(signUpDTO.getPassword());

    User user = User.builder()
        .username(username)
        .password(password)
        .email(email)
        .role(signUpDTO.getRole())
        .build();

    User createdUser = userRepository.save(user);

    return createdUser;
  }
}
