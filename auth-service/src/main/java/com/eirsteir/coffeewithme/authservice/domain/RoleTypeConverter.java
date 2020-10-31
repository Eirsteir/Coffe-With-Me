package com.eirsteir.coffeewithme.authservice.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

  @Override
  public String convertToDatabaseColumn(RoleType type) {
    return type == null ? null : type.name();
  }

  @Override
  public RoleType convertToEntityAttribute(String value) {
    if (value == null) return null;

    return RoleType.from(value);
  }
}
