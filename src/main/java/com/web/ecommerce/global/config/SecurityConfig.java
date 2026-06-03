package com.web.ecommerce.global.config;

import com.web.ecommerce.global.security.JwtAuthenticationFilter;
import com.web.ecommerce.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtProvider jwtProvider;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    configureFilters(http);
    configureAuthorization(http);
    return http.build();
  }

  /** 필터 및 기본 설정 */
  private void configureFilters(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(e -> e
            .authenticationEntryPoint((request, response, ex) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
        .addFilterBefore(new JwtAuthenticationFilter(jwtProvider),
            UsernamePasswordAuthenticationFilter.class);
  }

  /** 권한 설정 */
  private void configureAuthorization(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        // CORS preflight
        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // 누구나 접근 가능
        .requestMatchers("/api/users/signup", "/api/users/login", "/api/users/admin/signup", "/api/users/refresh", "/api/users/logout")
        .permitAll()

        .requestMatchers(HttpMethod.GET, "/api/products/**")
        .permitAll()

        .requestMatchers("/error")
        .permitAll()

        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**")
        .permitAll()

        .requestMatchers(HttpMethod.GET, "/api/coupons/claim")
        .permitAll()

        // 광고 노출/클릭은 일반 유저 접근 가능
        .requestMatchers(HttpMethod.POST, "/api/ads/*/expose").hasRole("USER")
        .requestMatchers(HttpMethod.PATCH, "/api/ads/*/click").hasRole("USER")

        // 일반 유저 쿠폰 관련 (본인 쿠폰만)
.requestMatchers(HttpMethod.POST, "/api/coupons/*/download").hasRole("USER")
        .requestMatchers(HttpMethod.GET, "/api/users/me/coupons").hasRole("USER")
        .requestMatchers(HttpMethod.PATCH, "/api/users/me/coupons/*/use").hasRole("USER")

        // 배송지
        .requestMatchers("/api/addresses", "/api/addresses/**").hasRole("USER")

        // 장바구니
        .requestMatchers("/api/cart", "/api/cart/**").hasRole("USER")

        // 찜
        .requestMatchers("/api/wishlist", "/api/wishlist/**").hasRole("USER")

        // 주문
        .requestMatchers("/api/orders", "/api/orders/**").hasRole("USER")

// 관리자만 가능
        .requestMatchers(HttpMethod.POST, "/api/products/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("ADMIN")
        .requestMatchers(RegexRequestMatcher.regexMatcher(".*/admin/.*")).hasRole("ADMIN")
        .requestMatchers("/api/campaigns", "/api/campaigns/**", "/api/events", "/api/events/**").hasRole("ADMIN")
        .requestMatchers("/api/coupons", "/api/coupons/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/ads").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/ads/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/ads/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/ads/select").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/users/*/ads").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/ads", "/api/ads/**").hasRole("ADMIN")
        // 나머지는 로그인 필요
        .anyRequest().authenticated()
    );
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
