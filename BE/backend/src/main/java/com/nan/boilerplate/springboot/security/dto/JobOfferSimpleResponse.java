package com.nan.boilerplate.springboot.security.dto;

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
//    private Company company;
}
