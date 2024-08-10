package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.exceptions.UserNotFoundException;
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

        User user = userService.findByUsername(loginRequest.getUsername());

        if (!user.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }



//      얼리 리턴 미적용
//        if (user.isActive()) {
//            try {
//                final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
//                return ResponseEntity.ok(loginResponse);
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//            }
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
    }

    @PostMapping("/Company")
    public ResponseEntity<LoginResponse> companyLoginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        Company company = userService.findByCompanyName(loginRequest.getUsername()).get();

        if (!company.isActive()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
