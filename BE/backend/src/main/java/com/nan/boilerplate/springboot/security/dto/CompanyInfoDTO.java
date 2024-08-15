package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyInfoDTO {
    private String username; // 아이디같은거
    private String companyName;

    @Length(min = 10, max = 10, message = "사업자등록번호 10자리를 입력하세요.")
    private String companyRegistrationNumber;

    private String phoneNum;
    private String companyAddress;

    public static CompanyInfoDTO toDTO(Company company) {
        return CompanyInfoDTO.builder()
                .username(company.getUsername())
                .companyName(company.getCompanyName())
                .companyRegistrationNumber(company.getCompanyRegistrationNumber())
                .phoneNum(company.getPhoneNum())
                .companyAddress(company.getCompanyAddress())
                .build();
    }
}