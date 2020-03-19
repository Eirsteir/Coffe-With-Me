package com.eirsteir.coffeewithme.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CoffeeRequestStatusConverterTest {

    private final CoffeeRequestStatusConverter
            COFFEE_REQUEST_STATUS_CONVERTER  = new CoffeeRequestStatusConverter();


    public static Stream<Arguments> coffeeRequestAttributeProvider() {
        return Stream.of(
                Arguments.of(CoffeeRequestStatus.PENDING, 1),
                Arguments.of(CoffeeRequestStatus.ACCEPTED, 2),
                Arguments.of(CoffeeRequestStatus.REJECTED, 3),
                Arguments.of(CoffeeRequestStatus.CHANGE_OF_TIME_REQUESTED, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("coffeeRequestAttributeProvider")
    public void convertToDatabaseColumn(CoffeeRequestStatus requestStatus, Integer value) {
        Integer convertedRequestStatus = COFFEE_REQUEST_STATUS_CONVERTER.convertToDatabaseColumn(requestStatus);

        assertThat(convertedRequestStatus).isEqualTo(value);
    }

    public static Stream<Arguments> coffeeRequestValueProvider() {
        return Stream.of(
                Arguments.of(1, CoffeeRequestStatus.PENDING),
                Arguments.of(2, CoffeeRequestStatus.ACCEPTED),
                Arguments.of(3, CoffeeRequestStatus.REJECTED),
                Arguments.of(4, CoffeeRequestStatus.CHANGE_OF_TIME_REQUESTED)
        );
    }

    @ParameterizedTest
    @MethodSource("coffeeRequestValueProvider")
    public void convertToEntityAttribute(Integer value, CoffeeRequestStatus requestStatus) {
        CoffeeRequestStatus convertedRequestStatusValue = COFFEE_REQUEST_STATUS_CONVERTER.convertToEntityAttribute(value);

        assertThat(convertedRequestStatusValue).isEqualTo(requestStatus);
    }
}