package com.nan.boilerplate.springboot.security.dto;

import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {

    private String message;

    private String username;

    private String password;

}
