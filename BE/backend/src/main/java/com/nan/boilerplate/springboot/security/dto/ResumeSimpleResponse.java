package com.nan.boilerplate.springboot.security.dto;

import com.nan.boilerplate.springboot.model.Resume;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeSimpleResponse {
    private Long id;
    private String title;

    public static ResumeSimpleResponse toDTO(Resume resume) {
        return ResumeSimpleResponse.builder()
                .id(resume.getId())
                .title(resume.getTitle())
                .build();
    }
}
