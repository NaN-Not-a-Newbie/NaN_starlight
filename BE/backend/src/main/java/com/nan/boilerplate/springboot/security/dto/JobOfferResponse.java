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

    private SalaryType salaryType;

    private Long salary;

    private String location;

    private Long career;

    private String companyName;
//    private Company company;

    private Education education;

//    private String message;
}
