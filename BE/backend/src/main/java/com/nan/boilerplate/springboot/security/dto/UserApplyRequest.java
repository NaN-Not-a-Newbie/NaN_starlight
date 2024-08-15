package com.nan.boilerplate.springboot.security.dto;


import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.Resume;
import lombok.*;
import org.springframework.security.core.parameters.P;

import javax.validation.constraints.Pattern;

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
