package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.CompanyInfoDTO;
import com.nan.boilerplate.springboot.security.dto.UserInfoDTO;
import com.nan.boilerplate.springboot.security.dto.UserInfoResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.JobOfferService;
import com.nan.boilerplate.springboot.service.PageableValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/me")
public class MemberInfoController {  // 회원정보 수정 컨트롤러
    private final UserService userService;

    @Autowired
    public MemberInfoController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/user")
    public ResponseEntity<UserInfoResponse> updateUserInfo(@Valid @RequestBody UserInfoDTO userRequest) {
        String myName = SecurityConstants.getAuthenticatedUsername();
        if (userService.findByUsername(myName).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userService.updateUserInfo(userRequest));
    }

    @PutMapping("/company")
    public ResponseEntity<CompanyInfoDTO> updateCompanyInfo(@Valid @RequestBody CompanyInfoDTO companyRequest) {
        String myName = SecurityConstants.getAuthenticatedUsername();

        if (userService.findByCompanyName(myName).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(userService.updateCompanyInfo(companyRequest));
    }

    @PostMapping("/pwCheck")
    public ResponseEntity<String> validPassword(String password) {
        String myName = SecurityConstants.getAuthenticatedUsername();
        if (userService.findByUsername(myName).isEmpty() && userService.findByCompanyName(myName).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 유저입니다.");
        }

        if (userService.findByUsername(myName).isEmpty()) {
            return ResponseEntity.ok(userService.validPassword(password, userService.findByCompanyName(myName).get().getPassword()));
        } else {
            return ResponseEntity.ok(userService.validPassword(password, userService.findByUsername(myName).get().getPassword()));
        }
    }

//    @DeleteMapping
//    public ResponseEntity<String> withdraw() {
//        String myName = SecurityConstants.getAuthenticatedUsername();
//        if (userService.findByUsername(myName).isEmpty() && userService.findByCompanyName(myName).isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 유저입니다.");
//        }
//
//        if (userService.findByUsername(myName).isEmpty()) {
//            Company company = userService.findByCompanyName(myName).get();
//            return ResponseEntity.ok(userService.withdraw(company.getUserRole(), company.getId()));
//        } else {
//            User user = userService.findByUsername(myName).get();
//            return ResponseEntity.ok(userService.withdraw(user.getUserRole(), user.getId()));
//        }
//    }
}





