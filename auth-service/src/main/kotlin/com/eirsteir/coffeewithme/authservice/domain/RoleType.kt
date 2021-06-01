package com.eirsteir.coffeewithme.authservice.domain

import java.util.stream.Stream

enum class RoleType {
    ROLE_USER, ROLE_ADMIN, ROLE_WRITER, ROLE_READER;

    companion object {
        fun from(name: String): RoleType {
            return Stream.of(*values())
                .filter { v: RoleType -> v.name == name }
                .findFirst()
                .orElseThrow { IllegalArgumentException() }
        }
    }
}