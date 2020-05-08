package com.eirsteir.coffeewithme.domain.meeting.enums;

import com.eirsteir.coffeewithme.domain.meeting.MeetingStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MeetingStatusTest {

    static Stream<Arguments> requestStatusValueProvider() {
        return Stream.of(
                Arguments.of(MeetingStatus.PENDING, 1),
                Arguments.of(MeetingStatus.ACCEPTED, 2),
                Arguments.of(MeetingStatus.REJECTED, 3),
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
                Arguments.of(MeetingStatus.PENDING, "PENDING"),
                Arguments.of(MeetingStatus.ACCEPTED, "ACCEPTED"),
                Arguments.of(MeetingStatus.REJECTED, "REJECTED"),
                Arguments.of(MeetingStatus.CHANGE_OF_TIME_REQUESTED, "CHANGE_OF_TIME_REQUESTED")
        );
    }

    @ParameterizedTest
    @MethodSource("requestStatusNameProvider")
    void testGetStatus(MeetingStatus requestStatus, String requestStatusName) {
        assertThat(requestStatus.getStatus()).isEqualTo(requestStatusName);
    }
}
