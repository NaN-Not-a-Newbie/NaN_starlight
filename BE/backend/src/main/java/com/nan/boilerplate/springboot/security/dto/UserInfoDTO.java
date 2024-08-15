package com.nan.boilerplate.springboot.security.dto;


import com.nan.boilerplate.springboot.exceptions.EnumPattern;
import com.nan.boilerplate.springboot.model.*;
import lombok.*;
import org.springframework.security.core.parameters.P;

import javax.swing.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private String username;

    @NotEmpty
    private String name;

    @NotEmpty
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "생년월일 양식에 맞지 않습니다.")
    private String birthday;

    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 양식에 맞지 않습니다.")
    private String phoneNum;

    @NotEmpty
    private boolean isMale;

    @NotEmpty
    private EnvEyesight envEyesight;

    @NotEmpty
    private EnvBothHands envBothHands;

    @NotEmpty
    private EnvHandWork envhandWork;

    @NotEmpty
    private EnvLiftPower envLiftPower;

    @NotEmpty
    private EnvStndWalk envStndWalk;

    @NotEmpty
    private EnvLstnTalk envLstnTalk;

    @NotEmpty
    private Education education;

    public static UserInfoDTO toDTO(User user) {
        return UserInfoDTO.builder()
                .username(user.getUsername())
                .name(user.getName())
                .birthday(user.getBirthday())
                .phoneNum(user.getPhoneNum())
                .isMale(user.isMale())
                .envEyesight(user.getEnvEyesight())
                .envBothHands(user.getEnvBothHands())
                .envhandWork(user.getEnvhandWork())
                .envLiftPower(user.getEnvLiftPower())
                .envStndWalk(user.getEnvStndWalk())
                .envLstnTalk(user.getEnvLstnTalk())
                .education(user.getEducation())
                .build();
    }
}
