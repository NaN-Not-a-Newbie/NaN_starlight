package com.nan.boilerplate.springboot.security.dto;


import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.Resume;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserApplyRequest {
    private boolean hire;
    private Long resumeId;
    private Long jobOfferId;
}
