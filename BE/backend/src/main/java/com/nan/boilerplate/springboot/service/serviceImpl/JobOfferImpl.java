package com.nan.boilerplate.springboot.service.serviceImpl;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.repository.JobOfferRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.JobOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobOfferImpl implements JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final UserService userService;


    @Autowired
    public JobOfferImpl(JobOfferRepository jobOfferRepository, UserService userService) {
        this.jobOfferRepository = jobOfferRepository;
        this.userService = userService;
    }


    @Override
    public List<JobOfferResponse> getAllJobOffers() {
        List<JobOffer> offers = jobOfferRepository.findAll();
        List<JobOfferResponse> offersResponses = new ArrayList<>();
        for (JobOffer offer : offers) {
             offersResponses.add(JobOfferResponse.builder()
                     .body(offer.getBody())
                     .location(offer.getLocation())
                     .title(offer.getTitle())
                     .career(offer.getCareer())
                     .company(offer.getCompany())
                     .salary(offer.getSalary())
                     .education(offer.getEducation())
                     .salaryType(offer.getSalaryType())
                     .build());
        }
        return offersResponses;
    }

    @Override
    public Optional<JobOffer> getJobOfferById(long id) {
        return jobOfferRepository.findById(id);
    }

    @Override
    public JobOfferResponse addJobOffer(JobOfferRequest jobOfferRequest) {
//        User writer = userService.findByUsername(SecurityConstants.getAuthenticatedUsername());
        JobOffer jobOffer = JobOffer.builder()
                .title(jobOfferRequest.getTitle())
                .body(jobOfferRequest.getBody())
                .career(jobOfferRequest.getCareer())
                .location(jobOfferRequest.getLocation())
                .company(jobOfferRequest.getCompany())
                .salary(jobOfferRequest.getSalary())
                .salaryType(jobOfferRequest.getSalaryType())
                .education(jobOfferRequest.getEducation())
                .build();
        jobOfferRepository.save(jobOffer);
        JobOfferResponse offerResponse= JobOfferResponse.builder()
                .title(jobOfferRequest.getTitle())
                .body(jobOfferRequest.getBody())
                .career(jobOfferRequest.getCareer())
                .location(jobOfferRequest.getLocation())
                .company(jobOfferRequest.getCompany())
                .salary(jobOfferRequest.getSalary())
                .salaryType(jobOfferRequest.getSalaryType())
                .education(jobOfferRequest.getEducation())
                .build();
        return offerResponse;
    }

    @Override
    public JobOfferResponse updateJobOffer(JobOfferRequest jobOfferRequest) {
        JobOffer jobOffer= JobOffer.builder()
                .title(jobOfferRequest.getTitle())
                .location(jobOfferRequest.getLocation())
                .education(jobOfferRequest.getEducation())
                .salaryType(jobOfferRequest.getSalaryType())
                .salary(jobOfferRequest.getSalary())
                .career(jobOfferRequest.getCareer())
                .body(jobOfferRequest.getBody())
                .build();
        jobOfferRepository.save(jobOffer);
        return JobOfferResponse.builder()
                .title(jobOfferRequest.getTitle())
                .location(jobOfferRequest.getLocation())
                .education(jobOfferRequest.getEducation())
                .salaryType(jobOfferRequest.getSalaryType())
                .salary(jobOfferRequest.getSalary())
                .career(jobOfferRequest.getCareer())
                .body(jobOfferRequest.getBody()).build();
    }

    @Override
    public void deleteJobOffer(long id) {
        jobOfferRepository.deleteById(id);
    }
}
