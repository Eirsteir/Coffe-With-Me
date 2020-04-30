package com.eirsteir.coffeewithme.domain.request.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class FriendRequestStatusConverterTest {

    private final FriendRequestStatusConverter
            FRIEND_REQUEST_STATUS_CONVERTER  = new FriendRequestStatusConverter();


    public static Stream<Arguments> coffeeRequestAttributeProvider() {
        return Stream.of(
                Arguments.of(FriendRequestStatus.PENDING, 1),
                Arguments.of(FriendRequestStatus.ACCEPTED, 2),
                Arguments.of(FriendRequestStatus.REJECTED, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("coffeeRequestAttributeProvider")
    public void convertToDatabaseColumn(FriendRequestStatus requestStatus, Integer value) {
        Integer convertedRequestStatus = FRIEND_REQUEST_STATUS_CONVERTER.convertToDatabaseColumn(requestStatus);

        assertThat(convertedRequestStatus).isEqualTo(value);
    }

    public static Stream<Arguments> coffeeRequestValueProvider() {
        return Stream.of(
                Arguments.of(1, FriendRequestStatus.PENDING),
                Arguments.of(2, FriendRequestStatus.ACCEPTED),
                Arguments.of(3, FriendRequestStatus.REJECTED)
        );
    }

    @ParameterizedTest
    @MethodSource("coffeeRequestValueProvider")
    public void convertToEntityAttribute(Integer value, FriendRequestStatus requestStatus) {
        FriendRequestStatus convertedRequestStatusValue = FRIEND_REQUEST_STATUS_CONVERTER.convertToEntityAttribute(value);

        assertThat(convertedRequestStatusValue).isEqualTo(requestStatus);
    }
}