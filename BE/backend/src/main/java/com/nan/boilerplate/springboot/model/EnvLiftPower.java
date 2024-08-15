package com.nan.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.nan.boilerplate.springboot.exceptions.BadRequestException;

import java.util.stream.Stream;

public enum EnvLiftPower {
    // 5Kg 이내의 물건, 5~20Kg의 물건, 20Kg 이상의 물건을 다룰 수 있음, 무관
    UNDER5, UNDER20, OVER20, DONTCARE;

    @JsonCreator
    public static EnvLiftPower fromValue(String value) {
        return Stream.of(EnvLiftPower.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Invalid value: " + value));
    }

//     @EnumPattern(regexp = "UNDER5|UNDER20|OVER20|DONTCARE", enumClass = EnvLiftPower.class, message = "입력된 형식이 유효하지 않습니다.")

}
