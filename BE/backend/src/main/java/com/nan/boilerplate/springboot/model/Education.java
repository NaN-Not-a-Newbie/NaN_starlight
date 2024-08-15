package com.nan.boilerplate.springboot.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;

import java.util.stream.Stream;

public enum Education {
    ELEM, MIDDLE, HIGH, UNIV, MASTER, DOCTOR,DONTCARE;

//    @JsonCreator
//    public static Education fromValue(String value) {
//        return Stream.of(Education.values())
//                .filter(e -> e.name().equalsIgnoreCase(value))
//                .findFirst()
//                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
//    }

//     @EnumPattern(regexp = "ELEM|MIDDLE|HIGH|UNIV|MASTER|DOCTOR|DONTCARE", enumClass = Education.class, message = "입력된 형식이 유효하지 않습니다.")
//


}
