package com.web.ecommerce.global.security;

import com.web.ecommerce.domain.user.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

  private final Long id;
  private final String loginId;
  private final String password;
  private final String role;
  private final boolean active;

  public CustomUserDetails(User user){
    this.id = user.getId();
    this.loginId = user.getLoginId();
    this.password = user.getPassword();
    this.role = user.getRole().name();
    this.active = user.getIsActive() == 1;
  }

  // JWT 권한 체크에 사용 (ROLE_USER, ROLE_ADMIN)
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role));
  }

  @Override
  public String getUsername(){ return loginId; }

  //계정 활성화 여부
  @Override
  public boolean isEnabled() { return active; }

  @Override public boolean isAccountNonExpired() { return true; }
  @Override public boolean isAccountNonLocked() { return true; }
  @Override public boolean isCredentialsNonExpired() { return true; }


}
