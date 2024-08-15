package com.nan.boilerplate.springboot.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeSimpleResponse {
    private Long id;
    private String title;
}
