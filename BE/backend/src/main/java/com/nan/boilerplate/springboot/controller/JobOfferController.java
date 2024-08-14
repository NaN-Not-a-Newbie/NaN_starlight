package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.JobOfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.net.URI;
import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/jobOffer")
public class JobOfferController {
    private final JobOfferService jobOfferService;

    @Autowired
    public JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping
    public ResponseEntity<List<JobOfferSimpleResponse>> getAllJobOffers(Pageable pageable) {
        List<JobOfferSimpleResponse> jobOfferResponses = jobOfferService.getAllJobOffers(pageable);
        return ResponseEntity.ok(jobOfferResponses);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<JobOfferSimpleResponse>> getAllJobOffersPage(Pageable pageable) {
        return ResponseEntity.ok(jobOfferService.getAllJobOffersPage(pageable));
    }

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

    @GetMapping("/initial")
    public ResponseEntity<List<JobOfferSimpleResponse>> initialJobOffer(Pageable pageable){
        List<JobOfferSimpleResponse> offers=jobOfferService.initialJobOffer(pageable);
//        size : 한 페이지당 담길 데이터의 양 ex) 10, 5, ...
//        page : size를 기준으로 몇번째 페이지인지? ex) 0, 1, ...
//        sort : 무엇을 기준으로 정렬할 것인지? ex) createdAt,DESC, description
//        page=0&size=10&sort=description,DESC
        return ResponseEntity.ok(offers);
    }

//    @GetMapping("/gove")
//    public ResponseEntity<Void> goveJobOffer(){
//        try{
//            jobOfferService.getOptialJobOffers();
//            return ResponseEntity.noContent().build();
//    }
//        catch (Exception e){
//            return ResponseEntity.notFound().build();
//        }
//    }
    //existjobOffer 개발하면 풀것 User.Role Staff로 설정
}
