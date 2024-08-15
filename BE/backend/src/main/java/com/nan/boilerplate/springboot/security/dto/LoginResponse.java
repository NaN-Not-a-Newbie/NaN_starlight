package com.nan.boilerplate.springboot.security.dto;

import lombok.*;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;

import javax.validation.constraints.NotEmpty;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String message;

}
