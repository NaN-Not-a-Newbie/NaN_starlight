package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.exceptions.EnumPattern;
import com.nan.boilerplate.springboot.model.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
public class UserInfoResponse {
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

    private String age;

}
