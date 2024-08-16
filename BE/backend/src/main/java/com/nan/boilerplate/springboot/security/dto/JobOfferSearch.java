package com.nan.boilerplate.springboot.security.dto;


import com.nan.boilerplate.springboot.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferSearch {
    private String title;

    private String body;

    private Long salary;

    private Long career;

    private Education education;

    private EnvEyesight envEyesight;

    private EnvHandWork envHandWork;

    private EnvStndWalk envStndWalk;

    private EnvLstnTalk envLstnTalk;

    private EnvLiftPower envLiftPower;

    private EnvBothHands envBothHands;
}
