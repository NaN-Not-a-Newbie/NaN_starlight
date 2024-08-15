package com.nan.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;

import java.util.stream.Stream;

public enum EnvLstnTalk {
    // 듣고 말하는 작업 어려움, 듣고 말하기에 어려움 없음, 간단한 듣고 말하기 가능, 무관
    HARD, NOPROBLEM, SIMPLETALLK, DONTCARE;

    @JsonCreator
    public static EnvLstnTalk fromValue(String value) {
        return Stream.of(EnvLstnTalk.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
    }

//     @EnumPattern(regexp = "HARD|NOPROBLEM|SIMPLETALLK|DONTCARE", enumClass = EnvLstnTalk.class, message = "입력된 형식이 유효하지 않습니다.")

}
