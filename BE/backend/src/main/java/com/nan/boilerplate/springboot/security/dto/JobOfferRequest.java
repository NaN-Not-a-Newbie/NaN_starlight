package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JobOfferRequest {
    private String title;

    private String Body;

    private Long salary;

    private String location;

    private Long career;

    private String cntctNo;
//    private String companyName;

    private SalaryType salaryType;

    private EnvEyesight envEyesight;

    private EnvBothHands envBothHands;

    private EnvHandWork envhandWork;

    private EnvLiftPower envLiftPower;

    private EnvStndWalk envStndWalk;

    private EnvLstnTalk envLstnTalk;

    private Education education;

    private String deadLine;
}
