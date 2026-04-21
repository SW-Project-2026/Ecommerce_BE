package com.web.ecommerce.global.security;

import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  //Spring Security가 로그인 시 자동으로 호출
  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
    User user = userRepository.findByLoginIdAndIsActive(loginId, 1)
        .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    return new CustomUserDetails(user);
  }
}
