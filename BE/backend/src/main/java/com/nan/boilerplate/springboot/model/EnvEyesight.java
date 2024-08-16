package com.nan.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;
import com.nan.boilerplate.springboot.exceptions.EnumPattern;

import java.util.stream.Stream;

public enum EnvEyesight {
    // 아주 작은 글씨, 일상적활동, 비교적 큰 인쇄물, 무관
    VERYSMALLCHAR, ADL, BIGHAR, DONTCARE;

//    @JsonCreator
//    public static EnvEyesight fromValue(String value) {
//        return Stream.of(EnvEyesight.values())
//                .filter(e -> e.name().equalsIgnoreCase(value))
//                .findFirst()
//                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
//    }

    //    @EnumPattern(regexp = "VERYSMALLCHAR|ADL|BIGHAR|DONTCARE", message = "입력된 형식이 유효하지 않습니다.", enumClass = EnvEyesight.class)

}
