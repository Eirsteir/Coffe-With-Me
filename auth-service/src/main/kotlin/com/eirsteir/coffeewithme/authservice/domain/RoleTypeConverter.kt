package com.eirsteir.coffeewithme.authservice.domain

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class RoleTypeConverter : AttributeConverter<RoleType, String> {

    override fun convertToDatabaseColumn(type: RoleType) = type.name

    override fun convertToEntityAttribute(value: String) = RoleType.from(value)

}