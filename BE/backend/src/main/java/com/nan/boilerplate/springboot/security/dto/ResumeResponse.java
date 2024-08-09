package com.nan.boilerplate.springboot.security.dto;


import com.nan.boilerplate.springboot.model.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {

    private String title;
    private String body;
    private User user;
    private String message;

}
