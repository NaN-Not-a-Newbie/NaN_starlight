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

    public static JobOfferResponse toDTO(JobOffer offer) {
        return JobOfferResponse.builder()
                .title(offer.getTitle())
                .career(offer.getCareer())
                .companyName(offer.getCompany().getCompanyName())
                .salary(offer.getSalary())
                .education(offer.getEducation())
                .envBothHands(offer.getEnvBothHands())
                .envEyesight(offer.getEnvEyesight())
                .envhandWork(offer.getEnvhandWork())
                .envLiftPower(offer.getEnvLiftPower())
                .envLstnTalk(offer.getEnvLstnTalk())
                .envStndWalk(offer.getEnvStndWalk())
                .salaryType(offer.getSalaryType())
                .body(offer.getBody())
                .location(offer.getLocation())
                .deadLine(offer.getDeadLine())
                .build();
    }
}
