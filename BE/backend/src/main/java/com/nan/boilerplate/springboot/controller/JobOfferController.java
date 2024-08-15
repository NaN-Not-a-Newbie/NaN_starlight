package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.exceptions.BadRequestException;
import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.JobOfferService;
import com.nan.boilerplate.springboot.service.PageableValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/jobOffer")
public class JobOfferController {
    private final JobOfferService jobOfferService;
    private final PageableValidationService pageableValidationService;

    @Autowired
    public JobOfferController(JobOfferService jobOfferService, PageableValidationService pageableValidationService) {
        this.jobOfferService = jobOfferService;
        this.pageableValidationService = pageableValidationService;
    }
    
    // 로그인 안 된 상태에서 모든 공고 페이징 불러오기
    @GetMapping
    public ResponseEntity<Page<JobOfferSimpleResponse>> getAllJobOffers(Pageable pageable) {
        try {

            Page<JobOfferSimpleResponse> page = jobOfferService
                    .getAllJobOffers(pageableValidationService.validateAndCorrectPageable(pageable));
            return ResponseEntity.ok(page);

        } catch (BadRequestException e) {
            log.error("Error occurred while fetching job offers page", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 컬럼입니다.");
        }
    }
    
    // 공고 자세히 보기
    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponse> getJobOfferById(@PathVariable Long id) {
        if (jobOfferService.getJobOfferById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        JobOffer offer = jobOfferService.getJobOfferById(id).get();
        return ResponseEntity.ok(JobOfferResponse.toDTO(offer));

    }

    @PostMapping
    public ResponseEntity<Void> addJobOffer(@RequestBody JobOfferRequest jobOfferRequest) {
        Long createdJobOfferId = jobOfferService.addJobOffer(jobOfferRequest);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create("/jobOffer/" + createdJobOfferId))
                .build();  // 리다이렉트
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOfferSimpleResponse> updateJobOffer(@PathVariable Long id, @RequestBody JobOfferRequest jobOfferRequest) {
        if (jobOfferService.getJobOfferById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String myName = SecurityConstants.getAuthenticatedUsername(); // 로그인 된 계정의 username
        String author = jobOfferService.getJobOfferById(id).get().getCompany().getUsername(); // 글 작성자

        if (!author.equals(myName)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(jobOfferService.updateJobOffer(id, jobOfferRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }

    // 로그인 된 상태에서 맞춤화된 공고 불러오기
    @GetMapping("/initial")
    public ResponseEntity<Page<JobOfferSimpleResponse>> initialJobOffer(Pageable pageable){
        try {
            return ResponseEntity.ok(jobOfferService.initialJobOffer(pageable));
        } catch (Exception e) {

            log.error("Error occurred while fetching job offers page", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 문제가 발생했습니다.");
        }
    }

//    @GetMapping("/gove")
//    public ResponseEntity<Void> goveJobOffer(){
//        try{
//            jobOfferService.getOfficialJobOffers();
//            return ResponseEntity.noContent().build();
//      }
//        catch (Exception e){
//            return ResponseEntity.notFound().build();
//        }
//    }
//    existjobOffer 개발하면 풀것 User.Role Staff로 설정
}
