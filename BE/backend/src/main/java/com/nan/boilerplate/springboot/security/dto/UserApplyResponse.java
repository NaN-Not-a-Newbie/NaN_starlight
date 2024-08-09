package com.nan.boilerplate.springboot.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserApplyResponse {
    private Long resumeId;
    private Long jobOfferId;
    private boolean hire;
    private String message;
}
