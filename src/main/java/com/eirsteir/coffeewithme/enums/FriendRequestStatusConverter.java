package com.eirsteir.coffeewithme.enums;

import com.eirsteir.coffeewithme.domain.RequestStatusConverter;

import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true) // automatically apply the conversion logic to all mapped attributes of a FriendRequestStatus type.
public class FriendRequestStatusConverter implements RequestStatusConverter<FriendRequestStatus, Integer> {

    public Integer convertToDatabaseColumn(FriendRequestStatus friendRequestStatus) {
        return friendRequestStatus == null ? null : friendRequestStatus.getValue();
    }


    public FriendRequestStatus convertToEntityAttribute(Integer value) {
        if  (value == null)
            return null;

        return Stream.of(FriendRequestStatus.values())
                .filter(v -> v.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
