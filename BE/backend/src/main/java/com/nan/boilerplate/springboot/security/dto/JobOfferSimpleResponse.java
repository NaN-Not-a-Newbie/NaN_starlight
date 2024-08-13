package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.SalaryType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class JobOfferSimpleResponse {
    private Long id;

    private String title;

    private String companyName;

    private String location;

    private SalaryType salaryType;

    private Long salary;

//    private String deadLine;

//    private Company company;
}
