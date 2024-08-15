package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse extends UserInfoDTO{

    private String age;

    @Builder(builderMethodName = "userInfoResponseBuilder")
    public UserInfoResponse(String username, String name, String birthday, String phoneNum, boolean isMale,
                            EnvEyesight envEyesight, EnvBothHands envBothHands, EnvHandWork envhandWork,
                            EnvLiftPower envLiftPower, EnvStndWalk envStndWalk, EnvLstnTalk envLstnTalk,
                            Education education, String age) {
        super(username, name, birthday, phoneNum, isMale, envEyesight, envBothHands, envhandWork,
                envLiftPower, envStndWalk, envLstnTalk, education);
        this.age = age;
    }

}
