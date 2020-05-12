package com.eirsteir.coffeewithme.domain.user;

import java.util.stream.Stream;

public enum UserType {
    GOOGLE,
    LOCAL;

    public static UserType from(String type) {
        return Stream.of(UserType.values())
                .filter(v -> v.name().equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);    }
}
