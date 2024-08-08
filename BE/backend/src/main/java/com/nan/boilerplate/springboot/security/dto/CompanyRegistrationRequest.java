package com.nan.boilerplate.springboot.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyRegistrationRequest {
    @NotEmpty(message = "...")
    private Long companyRegistrationNumber;

    @NotEmpty(message = "{registration_name_not_empty}")
    private String companyName;

    // 직종 -> 디비 만들고 추가

    @NotEmpty(message = "{registration_name_not_empty}")
    private String PhoneNum;

    @NotEmpty(message = "{registration_name_not_empty}")
    private String CompanyAddress;

    @NotEmpty(message = "{registration_username_not_empty}")
    private String username;

    @NotEmpty(message = "{registration_password_not_empty}")
    private String password;
}
