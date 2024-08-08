package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.Education;
import com.nan.boilerplate.springboot.model.SalaryType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class JobOfferResponse {
    private String title;

    private String body;

    private Long salary;

    private String location;

    private Long career;

    private Company company;
//    private Long companyId;

    private Education education;

    private SalaryType salaryType;

    private String message;
}
