package com.web.ecommerce.domain.event.entity;

import com.web.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "event")
public class Event extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_key", nullable = false, unique = true)
  private String eventKey;

  @Column(name ="event_name", nullable = false)
  private String eventName;

  @Column(name="description")
  private String description;

  @Column(name="is_active", nullable = false)
  private Boolean isActive;

  public void update(String eventKey, String eventName, String description, Boolean isActive) {
    this.eventKey = eventKey;
    this.eventName = eventName;
    this.description = description;
    this.isActive = isActive;
  }
}
