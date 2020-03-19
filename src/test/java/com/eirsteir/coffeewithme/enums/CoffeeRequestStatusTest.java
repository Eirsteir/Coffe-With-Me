package com.eirsteir.coffeewithme.enums;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CoffeeRequestStatusTest {

    static Stream<Arguments> requestStatusValueProvider() {
        return Stream.of(
                Arguments.of(CoffeeRequestStatus.PENDING, 1),
                Arguments.of(CoffeeRequestStatus.ACCEPTED, 2),
                Arguments.of(CoffeeRequestStatus.REJECTED, 3),
                Arguments.of(CoffeeRequestStatus.CHANGE_OF_TIME_REQUESTED, 4)
        );
    }

    @ParameterizedTest
    @MethodSource("requestStatusValueProvider")
    void testGetValue(CoffeeRequestStatus requestStatus, Integer requestStatusValue) {
        assertThat(requestStatus.getValue()).isEqualTo(requestStatusValue);
    }

    static Stream<Arguments> requestStatusNameProvider() {
        return Stream.of(
                Arguments.of(CoffeeRequestStatus.PENDING, "PENDING"),
                Arguments.of(CoffeeRequestStatus.ACCEPTED, "ACCEPTED"),
                Arguments.of(CoffeeRequestStatus.REJECTED, "REJECTED"),
                Arguments.of(CoffeeRequestStatus.CHANGE_OF_TIME_REQUESTED, "CHANGE_OF_TIME_REQUESTED")
        );
    }

    @ParameterizedTest
    @MethodSource("requestStatusNameProvider")
    void testGetStatus(CoffeeRequestStatus requestStatus, String requestStatusName) {
        assertThat(requestStatus.getStatus()).isEqualTo(requestStatusName);
    }
}
