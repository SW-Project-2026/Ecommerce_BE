package com.web.ecommerce.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.ecommerce.global.common.BaseTimeEntity;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User  extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column( nullable = false, length = 50)
  private String name;

  @Column(name = "login_id", nullable = false, unique = true, length = 50)
  private String loginId;

  @Column(nullable = false)
  @JsonIgnore
  private String password;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(length = 20)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  @Builder.Default
  private Role role = Role.USER;

  @Enumerated(EnumType.STRING)
  @Column(name = "grade", nullable = false, length = 10)
  @Builder.Default
  private UserGrade grade = UserGrade.NEW;

  @Column(name = "last_login_at")
  private LocalDateTime lastLoginAt;

  @Column(name = "is_active")
  private Integer isActive;

  @Column(name = "marketing_agreed", nullable = false)
  private boolean marketingAgreed;

  public void updateProfile(String name, String phone) {
    this.name = name;
    this.phone = phone;
  }

  public void updatePassword(String encodedPassword) {
    this.password = encodedPassword;
  }

  public void updateGrade(UserGrade grade) {
    this.grade = grade;
  }

  public void updateLastLoginAt() {
    this.lastLoginAt = LocalDateTime.now();
  }

  public void withdraw() {
    this.isActive = 0;
  }
}
