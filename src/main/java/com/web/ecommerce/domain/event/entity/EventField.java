package com.web.ecommerce.domain.event.entity;

import com.web.ecommerce.domain.event.enums.FieldType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "event_field")
public class EventField {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", nullable = false)
  private Event event;

  @Column(name = "field_name", nullable = false)
  private String fieldName;

  @Column(name = "field_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private FieldType fieldType;

  @Column(name = "is_required", nullable = false)
  private Boolean isRequired;

  @Column(name = "description")
  private String description;

  public void update(String fieldName, FieldType fieldType, Boolean isRequired, String description) {
    this.fieldName = fieldName;
    this.fieldType = fieldType;
    this.isRequired = isRequired;
    this.description = description;
  }
}
