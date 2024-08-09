package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.service.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<JobOfferResponse>> getAllJobOffers() {
        List<JobOfferResponse> jobOfferResponses = jobOfferService.getAllJobOffers();
        return ResponseEntity.ok(jobOfferResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOfferResponse> getJobOfferById(@PathVariable Long id) {
        if (jobOfferService.getJobOfferById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            JobOffer offer = jobOfferService.getJobOfferById(id).get();
            JobOfferResponse response = JobOfferResponse.builder()
                    .body(offer.getBody())
                    .location(offer.getLocation())
                    .title(offer.getTitle())
                    .career(offer.getCareer())
                    .company(offer.getCompany())
                    .salary(offer.getSalary())
                    .education(offer.getEducation())
                    .salaryType(offer.getSalaryType())
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping
    public ResponseEntity<JobOfferResponse> addJobOffer(@RequestBody JobOfferRequest jobOfferRequest) {
        return ResponseEntity.ok(jobOfferService.addJobOffer(jobOfferRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOfferResponse> updateJobOffer(@PathVariable Long id, @RequestBody JobOfferRequest jobOfferRequest) {
        return ResponseEntity.ok(jobOfferService.updateJobOffer(id, jobOfferRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobOffer(@PathVariable Long id) {
        jobOfferService.deleteJobOffer(id);
        return ResponseEntity.noContent().build();
    }
}
