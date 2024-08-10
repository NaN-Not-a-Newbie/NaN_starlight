package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.LoginRequest;
import com.nan.boilerplate.springboot.security.dto.LoginResponse;
import com.nan.boilerplate.springboot.security.jwt.JwtTokenService;
import com.nan.boilerplate.springboot.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    // 로그인
    @PostMapping("/User")
    public ResponseEntity<LoginResponse> userLoginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("1--------------");
        User user = userService.findByUsername(loginRequest.getUsername());

        System.out.println("1-----------------");

        if (user.isActive()) {
            System.out.println("12----------------");
            try {
                System.out.println("13-----------------");
                final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
                System.out.println("14-----------------");
                return ResponseEntity.ok(loginResponse);
            } catch (Exception e) {
                System.out.println("15-----------------");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("16----------------");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/Company")
    public ResponseEntity<LoginResponse> companyLoginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("1--------------");
        Company company = userService.findByCompanyName(loginRequest.getUsername());
        System.out.println("1-----------------");

        if (company.isActive()) {
            System.out.println("12----------------");
            try {
                System.out.println("13-----------------");
                final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
                System.out.println(loginResponse);
                System.out.println("14-----------------");
                return ResponseEntity.ok(loginResponse);
            } catch (Exception e) {
                System.out.println("15-----------------");
                System.out.println(e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("16----------------");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
