package com.kihong.health.persistence.service.base;

import com.kihong.health.persistence.model.User;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;

public interface BaseService {

  default Optional<User> getUserContext() {
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      return Optional.empty();
    }
    User user = (User) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
    return Optional.of(user);
  }
}
