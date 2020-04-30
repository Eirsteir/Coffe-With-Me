package com.eirsteir.coffeewithme.domain.enums;

import com.eirsteir.coffeewithme.domain.RequestStatusConverter;

import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true) // automatically apply the conversion logic to all mapped attributes of a FriendRequestStatus type.
public class CoffeeRequestStatusConverter implements RequestStatusConverter<CoffeeRequestStatus, Integer> {

    public Integer convertToDatabaseColumn(CoffeeRequestStatus coffeeRequestStatus) {
        return coffeeRequestStatus == null ? null : coffeeRequestStatus.getValue();
    }

    public CoffeeRequestStatus convertToEntityAttribute(Integer value) {
        if  (value == null)
            return null;

        return Stream.of(CoffeeRequestStatus.values())
                .filter(v -> v.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
