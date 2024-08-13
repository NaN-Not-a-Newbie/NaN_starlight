package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.*;
import com.nan.boilerplate.springboot.security.jwt.JwtTokenService;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final FileService fileService;
    @PostMapping("users")
    public ResponseEntity<LoginResponse> registrationRequest(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        String message=userService.registrationUser(userRegistrationRequest).getMessage();
        User user=userService.findByUsername(userRegistrationRequest.getUsername());
        LoginRequest loginRequest = LoginRequest.builder()
                .password(userRegistrationRequest.getPassword()).username(userRegistrationRequest.getUsername()).build();

        if (user.isActive()) {

            try {
                final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
                loginResponse.setMessage(message);
                return ResponseEntity.ok(loginResponse);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("company")
    public ResponseEntity<LoginResponse> registrationRequest(@Valid @RequestBody CompanyRegistrationRequest companyRegistrationRequest, RedirectAttributes redirectAttributes) {
        String message=userService.registrationCompany(companyRegistrationRequest).getMessage();
        Company company=userService.findByCompanyName(companyRegistrationRequest.getUsername()).get();
        LoginRequest loginRequest = LoginRequest.builder()
                .password(companyRegistrationRequest.getPassword()).username(companyRegistrationRequest.getUsername()).build();

        if (company.isActive()) {

            try {
                final LoginResponse loginResponse = jwtTokenService.getLoginResponse(loginRequest);
                loginResponse.setMessage(message);
                return ResponseEntity.ok(loginResponse);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping(value = "/company/IDcard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> CompanyIDOCR(@ModelAttribute MultipartFile file){

        String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
        return ResponseEntity.ok(fileService.NaverOCRCompany(file, boundary));
    }
}
