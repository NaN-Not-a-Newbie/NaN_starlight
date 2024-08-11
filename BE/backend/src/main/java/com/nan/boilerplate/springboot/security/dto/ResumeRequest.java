package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeRequest {
    private String title;
    private String body;
}
