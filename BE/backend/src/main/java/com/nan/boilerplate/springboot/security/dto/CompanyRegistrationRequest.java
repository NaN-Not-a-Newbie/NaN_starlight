package com.nan.boilerplate.springboot.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.logging.log4j.message.Message;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyRegistrationRequest {

    @NotEmpty(message = "사업자등록번호 10자리 입력")
    @Length(min = 10, max = 10, message = "사업자등록번호 10자리를 입력하세요.")
    private String companyRegistrationNumber; //사업자 등록 번호

    @NotEmpty(message = "{registration_name_not_empty}")
    private String companyName;

    // 직종 -> 디비 만들고 추가

    @NotEmpty(message = "{registration_name_not_empty}")
    @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 양식에 맞지 않습니다.")
    private String PhoneNum;

    @NotEmpty(message = "{registration_name_not_empty}")
    private String CompanyAddress;

    @NotEmpty(message = "{registration_username_not_empty}")
    private String username;

    @NotEmpty(message = "{registration_password_not_empty}")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,}"
            , message = "비밀번호는 8자 이상 영문 대 소문자, 숫자, 특수문자를 포함해야 합니다.")
    private String password;

    @NotEmpty(message = "{registration_password_not_empty}")
    private String password2; //검증용

}
