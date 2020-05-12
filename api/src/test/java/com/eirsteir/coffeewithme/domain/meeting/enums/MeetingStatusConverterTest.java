package com.eirsteir.coffeewithme.domain.meeting.enums;

import com.eirsteir.coffeewithme.domain.meeting.MeetingStatus;
import com.eirsteir.coffeewithme.domain.meeting.MeetingStatusConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MeetingStatusConverterTest {

    private final MeetingStatusConverter
            COFFEE_REQUEST_STATUS_CONVERTER  = new MeetingStatusConverter();


    public static Stream<Arguments> coffeeRequestAttributeProvider() {
        return Stream.of(
                Arguments.of(MeetingStatus.REQUESTED, 1),
                Arguments.of(MeetingStatus.ACCEPTED, 2),
                Arguments.of(MeetingStatus.DECLINED, 3),
                Arguments.of(MeetingStatus.CHANGE_OF_TIME_REQUESTED, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("coffeeRequestAttributeProvider")
    public void convertToDatabaseColumn(MeetingStatus requestStatus, Integer value) {
        Integer convertedRequestStatus = COFFEE_REQUEST_STATUS_CONVERTER.convertToDatabaseColumn(requestStatus);

        assertThat(convertedRequestStatus).isEqualTo(value);
    }

    public static Stream<Arguments> coffeeRequestValueProvider() {
        return Stream.of(
                Arguments.of(1, MeetingStatus.REQUESTED),
                Arguments.of(2, MeetingStatus.ACCEPTED),
                Arguments.of(3, MeetingStatus.DECLINED),
                Arguments.of(4, MeetingStatus.CHANGE_OF_TIME_REQUESTED)
        );
    }

    @ParameterizedTest
    @MethodSource("coffeeRequestValueProvider")
    public void convertToEntityAttribute(Integer value, MeetingStatus requestStatus) {
        MeetingStatus convertedRequestStatusValue = COFFEE_REQUEST_STATUS_CONVERTER.convertToEntityAttribute(value);

        assertThat(convertedRequestStatusValue).isEqualTo(requestStatus);
    }
}