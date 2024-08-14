package com.nan.boilerplate.springboot.security.dto;


import com.nan.boilerplate.springboot.model.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
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
}
