package com.eirsteir.coffeewithme.api.domain.meeting.enums;

import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatusConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FriendshipStatusConverterTest {

    private final FriendshipStatusConverter
            FRIEND_REQUEST_STATUS_CONVERTER  = new FriendshipStatusConverter();


    public static Stream<Arguments> coffeeRequestAttributeProvider() {
        return Stream.of(
                Arguments.of(FriendshipStatus.REQUESTED, 1),
                Arguments.of(FriendshipStatus.ACCEPTED, 2),
                Arguments.of(FriendshipStatus.DECLINED, 3),
                Arguments.of(FriendshipStatus.BLOCKED, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("coffeeRequestAttributeProvider")
    public void convertToDatabaseColumn(FriendshipStatus requestStatus, Integer value) {
        Integer convertedRequestStatus = FRIEND_REQUEST_STATUS_CONVERTER.convertToDatabaseColumn(requestStatus);

        assertThat(convertedRequestStatus).isEqualTo(value);
    }

    public static Stream<Arguments> coffeeRequestValueProvider() {
        return Stream.of(
                Arguments.of(1, FriendshipStatus.REQUESTED),
                Arguments.of(2, FriendshipStatus.ACCEPTED),
                Arguments.of(3, FriendshipStatus.DECLINED),
                Arguments.of(4, FriendshipStatus.BLOCKED)
        );
    }

    @ParameterizedTest
    @MethodSource("coffeeRequestValueProvider")
    public void convertToEntityAttribute(Integer value, FriendshipStatus requestStatus) {
        FriendshipStatus convertedRequestStatusValue = FRIEND_REQUEST_STATUS_CONVERTER.convertToEntityAttribute(value);

        assertThat(convertedRequestStatusValue).isEqualTo(requestStatus);
    }
}