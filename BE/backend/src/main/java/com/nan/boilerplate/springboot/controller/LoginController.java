package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.LoginRequest;
import com.nan.boilerplate.springboot.security.dto.LoginResponse;
import com.nan.boilerplate.springboot.security.jwt.JwtTokenService;
import com.nan.boilerplate.springboot.security.service.CompanyService;
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
    private final CompanyService companyService;

    // 로그인
    @PostMapping("/user")
    public ResponseEntity<LoginResponse> userLoginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~1");
        if (user.isActive()) {
            try {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~2");
                final LoginResponse loginResponse = jwtTokenService.getUserLoginResponse(loginRequest);
                System.out.println("~~~~~~~~~~~~~~~~~~~~~3");
                return ResponseEntity.ok(loginResponse);
            } catch (Exception e) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~4");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~5");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/company")
    public ResponseEntity<LoginResponse> companyLoginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        Company company = companyService.findByUsername(loginRequest.getUsername());

        if (company.isActive()) {
            try {
                final LoginResponse loginResponse = jwtTokenService.getCompanyLoginResponse(loginRequest);
                return ResponseEntity.ok(loginResponse);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

