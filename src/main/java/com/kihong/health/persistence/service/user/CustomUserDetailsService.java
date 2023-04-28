package com.kihong.health.persistence.service.user;

import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByEmail(email);
    if (optionalUser.isEmpty()) {
      throw new UsernameNotFoundException(
          "해당 이메일의 유저가 존재하지 않습니다."
      );
    } else {
      User user = optionalUser.get();
      return user;
    }
  }
}