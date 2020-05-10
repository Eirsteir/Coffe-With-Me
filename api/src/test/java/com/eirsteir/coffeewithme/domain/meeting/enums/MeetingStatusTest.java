package com.eirsteir.coffeewithme.domain.meeting.enums;

import com.eirsteir.coffeewithme.domain.meeting.MeetingStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MeetingStatusTest {

    static Stream<Arguments> requestStatusValueProvider() {
        return Stream.of(
                Arguments.of(MeetingStatus.REQUESTED, 1),
                Arguments.of(MeetingStatus.ACCEPTED, 2),
                Arguments.of(MeetingStatus.DECLINED, 3),
                Arguments.of(MeetingStatus.CHANGE_OF_TIME_REQUESTED, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("requestStatusValueProvider")
    void testGetValue(MeetingStatus requestStatus, Integer requestStatusValue) {
        assertThat(requestStatus.getValue()).isEqualTo(requestStatusValue);
    }

    static Stream<Arguments> requestStatusNameProvider() {
        return Stream.of(
                Arguments.of(MeetingStatus.REQUESTED, "REQUESTED"),
                Arguments.of(MeetingStatus.ACCEPTED, "ACCEPTED"),
                Arguments.of(MeetingStatus.DECLINED, "DECLINED"),
                Arguments.of(MeetingStatus.CHANGE_OF_TIME_REQUESTED, "CHANGE_OF_TIME_REQUESTED")
        );
    }

    @ParameterizedTest
    @MethodSource("requestStatusNameProvider")
    void testGetStatus(MeetingStatus requestStatus, String requestStatusName) {
        assertThat(requestStatus.getStatus()).isEqualTo(requestStatusName);
    }
}
