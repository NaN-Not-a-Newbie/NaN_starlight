package com.nan.boilerplate.springboot.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;

import java.util.stream.Stream;

public enum UserRole {
    USER, ADMIN, STAFF, COMPANY;

    @JsonCreator
    public static UserRole  fromValue(String value) {
        return Stream.of(UserRole.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
    }

//     @EnumPattern(regexp = "USER|ADMIN|STAFF|COMPANY", enumClass = UserRole.class, message = "입력된 형식이 유효하지 않습니다.")
}
