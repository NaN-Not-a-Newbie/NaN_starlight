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
    private final UserApplyService userApplyService;
    private final JobOfferService jobOfferService;


    @Autowired
    public MemberInfoController(UserService userService) {
        this.userService = userService;
        this.userApplyService = userApplyService;
        this.jobOfferService = jobOfferService;
    }

    // 지원한 유저정보 applicant UserInfoDTO
//    @GetMapping("/company")
//    public ResponseEntity<String> get() {
//        String myName = SecurityConstants.getAuthenticatedUsername();
//        Optional<Company> companyOptional = userService.findByCompanyName(myName);
//        if (companyOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
//        }
//        Company company = companyOptional.get();
//
//
//        userApplyService.getJobOfferByCompanyId(company.getId());
//    }

    @GetMapping("/jobOffers")
    public ResponseEntity<List<JobOffer>> getJobOffers() {

        Company company = userService.findByCompanyName(SecurityConstants.getAuthenticatedUsername()).get();
        List<JobOffer> jobOffers = jobOfferService.getJobOfferByCompanyId(company.getId());
        return ResponseEntity.ok(jobOfferService.getJobOfferByCompanyId(company.getId()));
//        // 이력서 모음
//        List<UserApply> applies = new ArrayList<>();
//        for (JobOffer jobOffer:jobOffers) {
//            applies.add(userApplyService.findByJobOfferId(jobOffer.getId()));
//        }
//
//
//        for (JobOffer jobOffer : jobOffers) {
//            ObjectNode jobOfferJson = objectMapper.createObjectNode();
//            jobOfferJson.put("jobOfferId", jobOffer.getId());
//            jobOfferJson.put("title", jobOffer.getTitle());
//
//            // 지원자 목록을 ArrayNode로 생성
//            ArrayNode applicantsArray = objectMapper.createArrayNode();
//
//            for (UserApply userApply : applies) {
//                ObjectNode applicantJson = objectMapper.createObjectNode();
//                User user = userApply.getResume().getUser();
//
//                applicantJson.put("name", user.getName());
//                applicantJson.put("birthday", user.getBirthday());
//                applicantJson.put("phoneNum", user.getPhoneNum());
//                applicantJson.put("isMale", user.isMale());
//                applicantJson.put("envEyesight", user.getEnvEyesight().toString());
//                applicantJson.put("envBothHands", user.getEnvBothHands().toString());
//                applicantJson.put("envhandWork", user.getEnvhandWork().toString());
//                applicantJson.put("envLiftPower", user.getEnvLiftPower().toString());
//                applicantJson.put("envStndWalk", user.getEnvStndWalk().toString());
//                applicantJson.put("envLstnTalk", user.getEnvLstnTalk().toString());
//                applicantJson.put("education", user.getEducation().toString());
//                // 지원자 정보를 applicantsArray에 추가
//                applicantsArray.add(applicantJson);
//            }
//
//            // 지원자 목록을 jobOfferJson에 추가
//            jobOfferJson.set("applicants", applicantsArray);
//
//            // jobOffer를 jobOffersArray에 추가
//            jobOffersArray.add(jobOfferJson);
//        }
//
//        // 최종적으로 jobOffersArray를 responseJson에 추가
//        responseJson.set("jobOffers", jobOffersArray);
//
//        return ResponseEntity.ok(responseJson);
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
            if(userOptional.get()==null){
                throw new UserNotFoundException("잘못된 접근입니다.");
            }
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
            if(companyOptional.get()==null){
                throw new UserNotFoundException("잘못된 접근입니다.");
            }
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
            if(userService.findByUsername(myName).get()==null){
                throw new BadRequestException("잘못된 접근입니다.");
            }
            return ResponseEntity.ok(userService.validPassword(password, userService.findByCompanyName(myName).get().getPassword()));
        } else {
            if(userService.findByUsername(myName).get()==null){
                throw new BadRequestException("잘못된 접근입니다.");
            }
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





