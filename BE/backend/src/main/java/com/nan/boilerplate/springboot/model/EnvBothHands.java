package com.nan.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;
import com.nan.boilerplate.springboot.exceptions.EnumPattern;

import java.util.stream.Stream;

public enum EnvBothHands {
    // 한손작업 가능, 한손보조작업 가능,양손작업 가능, 무관
    ONE, SUBHAND, BOTHAND, DONTCARE;

//    @JsonCreator
//    public static EnvBothHands fromValue(String value) {
//        return Stream.of(EnvBothHands.values())
//                .filter(e -> e.name().equalsIgnoreCase(value))
//                .findFirst()
//                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
//    }

//    @EnumPattern(regexp = "ONE|SUBHAND|BOTHAND|DONTCARE", message = "입력된 형식이 유효하지 않습니다.", enumClass = EnvBothHands.class)

}
