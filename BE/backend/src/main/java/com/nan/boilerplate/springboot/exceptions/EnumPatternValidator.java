package com.nan.boilerplate.springboot.exceptions;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class EnumPatternValidator implements ConstraintValidator<EnumPattern, String> {

    private Pattern pattern;
    private Class<? extends Enum<?>> enumClass;

    @Override
    public void initialize(EnumPattern constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.regexp());
        this.enumClass = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
//            return true; // null 값은 다른 애노테이션에 의해 체크되므로 여기선 true로 처리
            throw new BadRequestException("값을 입력하세요.");
        }

        boolean matchesPattern = pattern.matcher(value).matches();
        boolean isValidEnum = Stream.of(enumClass.getEnumConstants())
                .anyMatch(e -> e.name().equals(value));

        return matchesPattern && isValidEnum;
    }
}