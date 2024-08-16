package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.UserApply;
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

    public static UserApplyResponse toDTO(UserApply userApply) {
        return UserApplyResponse.builder()
                .resumeId(userApply.getResume().getId())
                .jobOfferId(userApply.getJobOffer().getId())
                .hire(userApply.isHire())
                .build();
    }
}
