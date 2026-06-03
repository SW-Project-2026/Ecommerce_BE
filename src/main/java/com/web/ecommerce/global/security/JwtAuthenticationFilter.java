package com.web.ecommerce.global.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = resolveToken(request);

    if (token != null) {
      try {
        if (jwtProvider.validateToken(token)) {
          Long userId = jwtProvider.getUserId(token);
          String loginId = jwtProvider.getLoginId(token);
          String role = jwtProvider.getRole(token);

          UserPrincipal principal = new UserPrincipal(userId, loginId);
          UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(
                  principal, null,
                  List.of(new SimpleGrantedAuthority("ROLE_" + role))
              );
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (RuntimeException e) {
        // 만료/무효 토큰 → 인증 없이 통과 (Security가 401/403 처리)
        SecurityContextHolder.clearContext();
      }
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    if (request.getCookies() == null) return null;
    for (Cookie cookie : request.getCookies()) {
      if ("accessToken".equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

}
