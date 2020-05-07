package com.eirsteir.coffeewithme.domain.request.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FriendRequestStatusTest {

    static Stream<Arguments> requestStatusValueProvider() {
        return Stream.of(
                Arguments.of(FriendRequestStatus.PENDING, 1),
                Arguments.of(FriendRequestStatus.ACCEPTED, 2),
                Arguments.of(FriendRequestStatus.REJECTED, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("requestStatusValueProvider")
    void getValue(FriendRequestStatus requestStatus, Integer requestStatusValue) {
        assertThat(requestStatus.getValue()).isEqualTo(requestStatusValue);
    }


    static Stream<Arguments> requestStatusNameProvider() {
        return Stream.of(
                Arguments.of(FriendRequestStatus.PENDING, "PENDING"),
                Arguments.of(FriendRequestStatus.ACCEPTED, "ACCEPTED"),
                Arguments.of(FriendRequestStatus.REJECTED, "REJECTED")
        );
    }
    @ParameterizedTest
    @MethodSource("requestStatusNameProvider")
    void getStatus(FriendRequestStatus requestStatus, String requestStatusName) {
        assertThat(requestStatus.getStatus()).isEqualTo(requestStatusName);
    }
}

