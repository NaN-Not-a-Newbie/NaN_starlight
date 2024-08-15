package com.nan.boilerplate.springboot.security.dto;


import com.nan.boilerplate.springboot.model.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private String username;
    private String name;
    private String birthday;
    private String phoneNum;
    private boolean isMale;
    private EnvEyesight envEyesight;
    private EnvBothHands envBothHands;
    private EnvHandWork envhandWork;
    private EnvLiftPower envLiftPower;
    private EnvStndWalk envStndWalk;
    private EnvLstnTalk envLstnTalk;
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
