package com.web.ecommerce.global.common;

import com.web.ecommerce.global.security.CustomUserDetails;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<Long> {  //SecurityContext에서 로그인한 유저 ID를 반환하는 구현체

  @Override
  public Optional<Long> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()
        || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
      return Optional.empty();
    }

    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
    return Optional.of(userDetails.getId());
  }
}
