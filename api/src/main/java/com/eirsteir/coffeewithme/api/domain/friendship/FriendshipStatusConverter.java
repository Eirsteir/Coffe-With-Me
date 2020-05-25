package com.eirsteir.coffeewithme.api.domain.friendship;

import com.eirsteir.coffeewithme.api.domain.RequestStatusConverter;

import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true) // automatically apply the conversion logic to all mapped attributes of a FriendRequestStatus type.
public class FriendshipStatusConverter implements RequestStatusConverter<FriendshipStatus, Integer> {

    public Integer convertToDatabaseColumn(FriendshipStatus friendshipStatus) {
        return friendshipStatus == null ? null : friendshipStatus.getValue();
    }


    public FriendshipStatus convertToEntityAttribute(Integer value) {
        if  (value == null)
            return null;

        return Stream.of(FriendshipStatus.values())
                .filter(v -> v.getValue().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
