package com.nan.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;

import java.util.stream.Stream;

public enum EnvHandWork {
    // 큰 물품 조립가능, 작은 물품 조립가능, 정밀한 작업가능, 무관
    BIG, SMALL, ACCURATE, DONTCARE;

//    @JsonCreator
//    public static EnvHandWork fromValue(String value) {
//        return Stream.of(EnvHandWork.values())
//                .filter(e -> e.name().equalsIgnoreCase(value))
//                .findFirst()
//                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
//    }

//     @EnumPattern(regexp = "BIG|SMALL|ACCURATE|DONTCARE", enumClass = EnvHandWork.class, message = "입력된 형식이 유효하지 않습니다.")

}
