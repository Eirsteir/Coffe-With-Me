package com.eirsteir.coffeewithme.domain.role;

import java.util.stream.Stream;

public enum RoleType {

    ROLE_USER,
    ROLE_ADMIN,
    ROLE_WRITER,
    ROLE_READER;

    public static RoleType from(String name) {
        return Stream.of(RoleType.values())
                .filter(v -> v.name().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
