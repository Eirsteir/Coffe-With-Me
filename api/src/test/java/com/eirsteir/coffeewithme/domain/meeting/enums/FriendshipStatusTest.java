package com.eirsteir.coffeewithme.domain.meeting.enums;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FriendshipStatusTest {

    static Stream<Arguments> requestStatusValueProvider() {
        return Stream.of(
                Arguments.of(FriendshipStatus.REQUESTED, 1),
                Arguments.of(FriendshipStatus.ACCEPTED, 2),
                Arguments.of(FriendshipStatus.BLOCKED, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("requestStatusValueProvider")
    void getValue(FriendshipStatus requestStatus, Integer requestStatusValue) {
        assertThat(requestStatus.getValue()).isEqualTo(requestStatusValue);
    }


    static Stream<Arguments> requestStatusNameProvider() {
        return Stream.of(
                Arguments.of(FriendshipStatus.REQUESTED, "PENDING"),
                Arguments.of(FriendshipStatus.ACCEPTED, "ACCEPTED"),
                Arguments.of(FriendshipStatus.BLOCKED, "BLOCKED")
        );
    }
    @ParameterizedTest
    @MethodSource("requestStatusNameProvider")
    void getStatus(FriendshipStatus requestStatus, String requestStatusName) {
        assertThat(requestStatus.getStatus()).isEqualTo(requestStatusName);
    }
}

