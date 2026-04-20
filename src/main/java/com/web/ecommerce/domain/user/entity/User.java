package com.web.ecommerce.domain.user.entity;

import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  private String password;

  @Column(nullable = false, unique = true, length = 100)
  private String email;

  @Column(length = 20)
  private String phone;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private Role role;

  @Column(name = "is_active")
  private Integer isActive;

}
