package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Builder
@Setter
public class JobOfferResponse {
    private String title;

    private String body;

    private Long salary;

    private String location;

    private Long career;

    private String companyName;
//    private Company company;

    private EnvEyesight envEyesight;

    private EnvBothHands envBothHands;

    private EnvHandWork envhandWork;

    private SalaryType salaryType;

    private EnvLiftPower envLiftPower;

    private EnvStndWalk envStndWalk;

    private EnvLstnTalk envLstnTalk;

    private Education education;

    private String deadLine;

//    private String message;
}
