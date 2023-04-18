package com.kihong.health.persistence.service.user;

import com.kihong.health.persistence.dto.user.SignUpDTO;
import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.service.base.BaseService;

public interface UserService extends BaseService {

  User createUser(SignUpDTO signUpDTO);
}
