package com.web.ecommerce.domain.campaign.enums;

import com.web.ecommerce.domain.event.enums.FieldType;
import java.util.Set;

public enum Operator {

  EQUALS(Set.of(FieldType.STRING, FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE, FieldType.TIME)),
  NOT_EQUALS(Set.of(FieldType.STRING, FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE, FieldType.TIME)),
  CONTAINS(Set.of(FieldType.STRING)),
  NOT_CONTAINS(Set.of(FieldType.STRING)),
  GT(Set.of(FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE, FieldType.TIME)),
  GTE(Set.of(FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE, FieldType.TIME)),
  LT(Set.of(FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE, FieldType.TIME)),
  LTE(Set.of(FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE, FieldType.TIME)),
  BETWEEN(Set.of(FieldType.NUMBER, FieldType.DATETIME, FieldType.DATE, FieldType.TIME));

  private final Set<FieldType> supportedTypes;

  Operator(Set<FieldType> supportedTypes) {
    this.supportedTypes = supportedTypes;
  }

  public boolean supports(FieldType fieldType) {
    return supportedTypes.contains(fieldType);
  }
}
