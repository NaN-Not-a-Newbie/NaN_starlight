package com.nan.boilerplate.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/me")
public class MemberInfoController {  // 회원정보 수정 컨트롤러
    private final UserService userService;

    @Autowired
    public MemberInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ObjectNode> getInfo() {
        String myName = SecurityConstants.getAuthenticatedUsername();

        Optional<User> userOptional = userService.findByUsername(myName);
        Optional<Company> companyOptional = userService.findByCompanyName(myName);

        if (userOptional.isEmpty() && companyOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode json = objectMapper.createObjectNode();
        ObjectNode information = objectMapper.createObjectNode();

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // JSON 데이터 구성
            json.put("role", user.getUserRole().toString());

            // information 객체 구성
            information.put("username", user.getUsername());
            information.put("name", user.getName());
            information.put("birthday", user.getBirthday());
            information.put("phoneNum", user.getPhoneNum());
            information.put("isMale", user.isMale());
            information.put("envEyesight", user.getEnvEyesight().toString());
            information.put("envBothHands", user.getEnvBothHands().toString());
            information.put("envhandWork", user.getEnvhandWork().toString());
            information.put("envLiftPower", user.getEnvLiftPower().toString());
            information.put("envStndWalk", user.getEnvStndWalk().toString());
            information.put("envLstnTalk", user.getEnvLstnTalk().toString());
            information.put("education", user.getEducation().toString());

            json.set("information", information);

        } else {
            Company company = companyOptional.get();

            // JSON 데이터 구성
            json.put("role", company.getUserRole().toString());

            // information 객체 구성
            information.put("username", company.getUsername());
            information.put("companyName", company.getCompanyName());
            information.put("companyRegistrationNumber", company.getCompanyRegistrationNumber());
            information.put("phoneNum", company.getPhoneNum());
            information.put("companyAddress", company.getCompanyAddress());

            json.set("information", information);
        }

        return ResponseEntity.ok(json);
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





