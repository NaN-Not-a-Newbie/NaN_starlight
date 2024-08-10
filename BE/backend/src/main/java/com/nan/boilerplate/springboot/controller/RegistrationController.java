package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.security.dto.CompanyRegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.UserRegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.RegistrationResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    @PostMapping("users")
    public ResponseEntity<RegistrationResponse> registrationRequest(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {

        final RegistrationResponse registrationResponse = userService.registrationUser(userRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }
    @PostMapping("company")
    public ResponseEntity<RegistrationResponse> registrationRequest(@Valid @RequestBody CompanyRegistrationRequest companyRegistrationRequest) {
        final RegistrationResponse registrationResponse = userService.registrationCompany(companyRegistrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

}
