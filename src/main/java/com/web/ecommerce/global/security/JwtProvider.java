package com.web.ecommerce.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {
  private final SecretKey secretKey;
  private final long accessTokenExpiry;
  private final long refreshTokenExpiry;

  public JwtProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-expiry}") long accessTokenExpiry,
      @Value("${jwt.refresh-token-expiry}") long refreshTokenExpiry) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    this.accessTokenExpiry = accessTokenExpiry;
    this.refreshTokenExpiry = refreshTokenExpiry;
  }

  //AccessTocken 생성
  public String generateAccessToken(Long userId, String role){
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
        .signWith(secretKey)
        .compact();
  }
  // RefreshToken 생성
  public String generateRefreshToken(Long userId) {
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiry))
        .signWith(secretKey)
        .compact();
  }

  // 토큰에서 userId 추출
  public Long getUserId(String token) {
    return Long.parseLong(getClaims(token).getSubject());
  }

  // 토큰에서 role 추출
  public String getRole(String token) {
    return getClaims(token).get("role", String.class);
  }

  // 토큰 유효성 검증
  public boolean validateToken(String token) {
    try {
      getClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      throw new RuntimeException("EXPIRED_TOKEN");
    } catch (JwtException e) {
      throw new RuntimeException("INVALID_TOKEN");
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }



}
