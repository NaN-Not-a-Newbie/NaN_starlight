package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.security.dto.CompanyRegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.RegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.RegistrationResponse;
import com.nan.boilerplate.springboot.security.dto.UserRegistrationRequest;
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

    @PostMapping("/company")
    public ResponseEntity<RegistrationResponse> registrationRequestCompany(@Valid @RequestBody CompanyRegistrationRequest registrationRequest) {

        final RegistrationResponse registrationResponse = userService.registrationCompany(registrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

    @PostMapping("/user")
    public ResponseEntity<RegistrationResponse> registrationRequestUser(@Valid @RequestBody UserRegistrationRequest registrationRequest) {

        final RegistrationResponse registrationResponse = userService.registrationUser(registrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

}
