package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.exceptions.UserNotFoundException;
import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.*;
import com.nan.boilerplate.springboot.security.jwt.JwtTokenService;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
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
    @PostMapping(value = "/users")
    public ResponseEntity<LoginResponse> registrationRequest(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest){

        String message = userService.registrationUser(userRegistrationRequest).getMessage();

        if(userService.findByUsername(userRegistrationRequest.getUsername()).get()==null){
            throw new UsernameNotFoundException("잘못된 접근입니다.");
        }
        User user = userService.findByUsername(userRegistrationRequest.getUsername()).get();
//        fileService.backgroundCutout(multipartFile.getInputStream(), userRegistrationRequest.getUsername());
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

    @PostMapping(value = "/user/sign/{token}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> signUpRequest(@RequestPart MultipartFile file,@PathVariable String token) {
        try {
            //token base64디코딩
//            System.out.println(file.getBytes().toString());
            if(fileService.fileCheck(file)) {
                fileService.backgroundCutout(file.getInputStream(), token);
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping(value = "company")
    public ResponseEntity<LoginResponse> registrationRequest(@Valid @RequestBody CompanyRegistrationRequest companyRegistrationRequest) {
        String message=userService.registrationCompany(companyRegistrationRequest).getMessage();
        if(userService.findByCompanyName(companyRegistrationRequest.getCompanyName())==null){
            throw new UserNotFoundException("잘못된 접근입니다.");
        }
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
    @PostMapping(value = "/company/idCard", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> CompanyIDOCR(@ModelAttribute MultipartFile file){
        //MultipartFile null일 때 예외처리
        if (file==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        else{
            if(fileService.fileCheck(file)){
                String boundary = "----" + UUID.randomUUID().toString().replaceAll("-", "");
                List<String> stringList=fileService.NaverOCRCompany(file, boundary);
                for (String s : stringList){
                    if(s==""){
                        System.out.println(s);
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
                }
                return ResponseEntity.ok(fileService.NaverOCRCompany(file, boundary));
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }
}
