package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserRegistrationRequest {
    @NotEmpty(message = "{registration_name_not_empty}")
    private String name;

    @NotEmpty(message = "{registration_username_not_empty}")
    private String username;

    @NotEmpty(message = "{registration_password_not_empty}")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}"
            , message = "비밀번호는 8자 이상 영문 대 소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password; // 알파벳 대.소문자, 특수문자, 숫자 모두 포함한 8~자리

    @NotEmpty
    private String password2; // 검증용

    @NotEmpty
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "생년월일 양식에 맞지 않습니다.")
    private String birthday;

    @NotEmpty
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 양식에 맞지 않습니다.")
    private String PhoneNum;

    private boolean isMale;

    @Enumerated(EnumType.STRING)
    private EnvEyesight envEyesight;

    @Enumerated(EnumType.STRING)
    private EnvBothHands envBothHands;

    @Enumerated(EnumType.STRING)
    private EnvHandWork envhandWork;

    @Enumerated(EnumType.STRING)
    private EnvLiftPower envLiftPower;

    @Enumerated(EnumType.STRING)
    private EnvStndWalk envStndWalk;


    @Enumerated(EnumType.STRING)
    private EnvLstnTalk envLstnTalk;


    @Enumerated(EnumType.STRING)
    private Education education;
}