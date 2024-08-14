package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.*;
import com.nan.boilerplate.springboot.security.jwt.JwtTokenService;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Valid;
import java.util.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final FileService fileService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @PostMapping("users")
    public ResponseEntity<LoginFailResponse> registrationRequest(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        // 회원가입 로직
        String message = userService.registrationUser(userRegistrationRequest).getMessage();
        LoginRequest loginRequest = LoginRequest.builder()
                .password(userRegistrationRequest.getPassword()).username(userRegistrationRequest.getUsername()).build();
        
        // 로그인 로직
        if (userService.findByUsername(loginRequest.getUsername()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginFailResponse("존재하지 않는 아이디 입니다."));
        }

        User user = userService.findByUsername(loginRequest.getUsername()).get();

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginFailResponse("잘못된 비밀번호 입니다."));
        }

        if (user.isActive()) {
            final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
            loginResponse.setMessage("Welcome User");

            return ResponseEntity.ok(loginResponse);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginFailResponse("로그인 권한이 없습니다. 관리자에게 문의하세요."));
        }

    }


    @PostMapping("company")
    public ResponseEntity<LoginFailResponse> registrationRequest(@Valid @RequestBody CompanyRegistrationRequest companyRegistrationRequest, RedirectAttributes redirectAttributes) {
        String message = userService.registrationCompany(companyRegistrationRequest).getMessage();
        LoginRequest loginRequest = LoginRequest.builder()
                .password(companyRegistrationRequest.getPassword()).username(companyRegistrationRequest.getUsername()).build();

        // 로그인 로직
        if (userService.findByCompanyName(loginRequest.getUsername()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginFailResponse("존재하지 않는 아이디 입니다."));
        }

        Company company = userService.findByCompanyName(loginRequest.getUsername()).get();

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), company.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginFailResponse("잘못된 비밀번호 입니다."));
        }

        if (company.isActive()) {

            final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
            loginResponse.setMessage("Welcome Company");
            return ResponseEntity.ok(loginResponse);

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginFailResponse("로그인 권한이 없습니다. 관리자에게 문의하세요."));
        }
    }

    @PostMapping(value = "/company/IDcard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> CompanyIDOCR(@ModelAttribute MultipartFile file){

        String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
        return ResponseEntity.ok(fileService.NaverOCRCompany(file, boundary));
    }
}
