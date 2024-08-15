package com.nan.boilerplate.springboot.security.dto;


import com.nan.boilerplate.springboot.model.Education;
import com.nan.boilerplate.springboot.model.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {

    private String title;
    private String name;
    private String birthday;
    private String phoneNum;
    private Education education;
    private String body;

}
