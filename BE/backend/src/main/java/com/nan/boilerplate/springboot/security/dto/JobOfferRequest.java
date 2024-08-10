package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.Education;
import com.nan.boilerplate.springboot.model.SalaryType;
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

    private Long companyId;

    private SalaryType salaryType;

    private Education education;
}
