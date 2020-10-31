package com.eirsteir.coffeewithme.social.domain.friendship;

import com.eirsteir.coffeewithme.social.domain.RequestStatusConverter;
import java.util.stream.Stream;
import javax.persistence.Converter;

@Converter(
    autoApply = true) // automatically apply the conversion logic to all mapped attributes of a
// FriendRequestStatus type.
public class FriendshipStatusConverter
    implements RequestStatusConverter<FriendshipStatus, Integer> {

  public Integer convertToDatabaseColumn(FriendshipStatus friendshipStatus) {
    return friendshipStatus == null ? null : friendshipStatus.getValue();
  }

  public FriendshipStatus convertToEntityAttribute(Integer value) {
    if (value == null) return null;

    return Stream.of(FriendshipStatus.values())
        .filter(v -> v.getValue().equals(value))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new);
  }
}
