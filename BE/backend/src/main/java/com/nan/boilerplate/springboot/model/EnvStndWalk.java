package com.nan.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;

import java.util.stream.Stream;

public enum EnvStndWalk {
    // 오랫동안 가능, 서거나 걷는 일이 어려움, 일부 서서하는 작업 가능, 무관
    LONG, HARD, PART,  DONTCARE;

    @JsonCreator
    public static EnvStndWalk fromValue(String value) {
        return Stream.of(EnvStndWalk.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
    }

//     @EnumPattern(regexp = "LONG|HARD|PART|DONTCARE", enumClass = EnvStndWalk.class, message = "입력된 형식이 유효하지 않습니다.")

}
