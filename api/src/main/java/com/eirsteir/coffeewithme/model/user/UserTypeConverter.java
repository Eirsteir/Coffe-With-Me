package com.eirsteir.coffeewithme.domain.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, String> {

    @Override
    public String convertToDatabaseColumn(UserType type) {
        return type == null ? null : type.name();
    }

    @Override
    public UserType convertToEntityAttribute(String value) {
        if  (value == null)
            return null;

        return UserType.from(value);
    }
}
