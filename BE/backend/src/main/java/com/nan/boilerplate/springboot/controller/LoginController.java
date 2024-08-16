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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    // 로그인
    @PostMapping("/user")
    public ResponseEntity<LoginResponse> userLoginRequest(@Valid @RequestBody LoginRequest loginRequest) {

        if (userService.findByUsername(loginRequest.getUsername()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("존재하지 않는 아이디 입니다."));
        }
        if(userService.findByUsername(loginRequest.getUsername()).get()==null){
            throw new UserNotFoundException("잘못된 접근입니다.");
        }
        User user = userService.findByUsername(loginRequest.getUsername()).get();

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("잘못된 비밀번호 입니다."));
        }

        if (user.isActive()) {
                final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
                loginResponse.setMessage("Welcome User");
                return ResponseEntity.ok(loginResponse);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("로그인 권한이 없습니다. 관리자에게 문의하세요."));
        }
    }

    @PostMapping("/company")
    public ResponseEntity<LoginResponse> companyLoginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        if (userService.findByCompanyName(loginRequest.getUsername()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("존재하지 않는 아이디 입니다."));

        }
        if(userService.findByCompanyName(loginRequest.getUsername()).get()==null){
            throw new UserNotFoundException("잘못된 접근입니다.");
        }
        Company company = userService.findByCompanyName(loginRequest.getUsername()).get();

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), company.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("잘못된 비밀번호 입니다."));
        }

        if (company.isActive()) {

                final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
                loginResponse.setMessage("Welcome Company");
                return ResponseEntity.ok(loginResponse);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("로그인 권한이 없습니다. 관리자에게 문의하세요."));
        }
    }
}
