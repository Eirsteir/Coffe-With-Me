package com.eirsteir.coffeewithme.social.domain.friendship

import com.eirsteir.coffeewithme.social.domain.RequestStatusConverter
import java.util.stream.Stream
import javax.persistence.Converter

@Converter(autoApply = true)
class FriendshipStatusConverter : RequestStatusConverter<FriendshipStatus, Int> {

    override fun convertToDatabaseColumn(friendshipStatus: FriendshipStatus) = friendshipStatus.getValue()

    override fun convertToEntityAttribute(value: Int): FriendshipStatus =
        FriendshipStatus.values()
            .filter { v -> v.getValue() == value }
            .stream()
            .findFirst()
            .orElseThrow { IllegalArgumentException() }

}