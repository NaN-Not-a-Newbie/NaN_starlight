package com.nan.boilerplate.springboot.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginResponse extends LoginFailResponse{
    private String token;

    public LoginResponse(String token, String message) {
        super(message);
        this.token = token;
    }
}
