package com.web.ecommerce.domain.campaign.enums;

import com.web.ecommerce.domain.event.enums.FieldType;
import java.util.Set;

public enum Operator {

  EQUALS(Set.of(FieldType.STRING, FieldType.NUMBER, FieldType.DATETIME)),
  NOT_EQUALS(Set.of(FieldType.STRING, FieldType.NUMBER, FieldType.DATETIME)),
  CONTAINS(Set.of(FieldType.STRING)),
  NOT_CONTAINS(Set.of(FieldType.STRING)),
  GT(Set.of(FieldType.NUMBER, FieldType.DATETIME)),
  GTE(Set.of(FieldType.NUMBER, FieldType.DATETIME)),
  LT(Set.of(FieldType.NUMBER, FieldType.DATETIME)),
  LTE(Set.of(FieldType.NUMBER, FieldType.DATETIME)),
  BETWEEN(Set.of(FieldType.NUMBER, FieldType.DATETIME));

  private final Set<FieldType> supportedTypes;

  Operator(Set<FieldType> supportedTypes) {
    this.supportedTypes = supportedTypes;
  }

  public boolean supports(FieldType fieldType) {
    return supportedTypes.contains(fieldType);
  }
}
