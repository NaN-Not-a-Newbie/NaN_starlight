package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
@AllArgsConstructor
public class JobOfferSimpleResponse {
    private Long id;

    private String companyName;

    private String title;

    private SalaryType salaryType;

    private Long salary;

    private Long career;

    private Education education;

    private EnvEyesight envEyesight;

    private EnvBothHands envBothHands;

    private EnvHandWork envhandWork;

    private EnvLiftPower envLiftPower;

    private EnvStndWalk envStndWalk;

    private EnvLstnTalk envLstnTalk;

    private String location;

    private String deadLine;

    public static JobOfferSimpleResponse toDTO(JobOffer offer) {
        return JobOfferSimpleResponse.builder()
                .id(offer.getId())
                .companyName(offer.getCompany().getCompanyName())
                .title(offer.getTitle())
                .salaryType(offer.getSalaryType())
                .salary(offer.getSalary())
                .career(offer.getSalary())
                .education(offer.getEducation())
                .envEyesight(offer.getEnvEyesight())
                .envBothHands(offer.getEnvBothHands())
                .envhandWork(offer.getEnvhandWork())
                .envLiftPower(offer.getEnvLiftPower())
                .envStndWalk(offer.getEnvStndWalk())
                .envLstnTalk(offer.getEnvLstnTalk())
                .location(offer.getLocation())
                .deadLine(offer.getDeadLine())
                .build();
    }

}
