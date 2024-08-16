package com.nan.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;

import java.util.stream.Stream;

public enum SalaryType {
    // 시급, 주급, 월급, 연봉
    TIME, WEEK, MONTH, YEAR;

    @JsonCreator
    public static SalaryType fromValue(String value) {
        return Stream.of(SalaryType.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
    }

//     @EnumPattern(regexp = "TIME|WEEK|MONTH|YEAR", enumClass = SalaryType.class, message = "입력된 형식이 유효하지 않습니다.")

}
