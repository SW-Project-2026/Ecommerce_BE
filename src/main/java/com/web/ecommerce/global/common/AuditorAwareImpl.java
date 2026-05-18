package com.web.ecommerce.global.common;

import com.web.ecommerce.global.security.UserPrincipal;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

    Object principal = authentication.getPrincipal();
    if (principal instanceof UserPrincipal userPrincipal) {
      return Optional.of(userPrincipal.loginId());
    }

    return Optional.empty();
  }
}
