package com.nan.boilerplate.springboot.service.Impl;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.repository.CompanyRepository;
import com.nan.boilerplate.springboot.repository.JobOfferRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobOfferServiceImpl implements JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<JobOfferSimpleResponse> getAllJobOffers() {
        List<JobOffer> offers = jobOfferRepository.findAll();
        List<JobOfferSimpleResponse> offersResponses = new ArrayList<>();
        for (JobOffer offer : offers) {
            offersResponses.add(JobOfferSimpleResponse.builder()
                    .title(offer.getTitle())
                    .companyName(offer.getCompany().getCompanyName())
                    .build());
        }
        return offersResponses;
    }

    @Override
    public Optional<JobOffer> getJobOfferById(long id) {
        return jobOfferRepository.findById(id);
    }

    @Override
    public Long addJobOffer(JobOfferRequest jobOfferRequest) {
//        User writer = userService.findByUsername(SecurityConstants.getAuthenticatedUsername());
        JobOffer jobOffer = JobOffer.builder()
                .title(jobOfferRequest.getTitle())
                .body(jobOfferRequest.getBody())
                .career(jobOfferRequest.getCareer())
                .location(jobOfferRequest.getLocation())
                .company(companyRepository.findByUsername(jobOfferRequest.getCompanyName()).get())
                .salary(jobOfferRequest.getSalary())
                .salaryType(jobOfferRequest.getSalaryType())
                .education(jobOfferRequest.getEducation())
                .envEyesight(jobOfferRequest.getEnvEyesight())
                .envhandWork(jobOfferRequest.getEnvhandWork())
                .envLiftPower(jobOfferRequest.getEnvLiftPower())
                .envBothHands(jobOfferRequest.getEnvBothHands())
                .envStndWalk(jobOfferRequest.getEnvStndWalk())
                .envLstnTalk(jobOfferRequest.getEnvLstnTalk())
                .build();
//        jobOfferRepository.save(jobOffer);
        return jobOfferRepository.save(jobOffer).getId();
//        JobOfferResponse jobOfferResponse = JobOfferResponse.builder()
//                .message("Add Success")
//                .build();
//        return jobOfferResponse;
    }

    @Override
    public JobOfferResponse updateJobOffer(Long id, JobOfferRequest jobOfferRequest) {
        if (!jobOfferRepository.existsById(id)) {
            return JobOfferResponse.builder()
                    .message("Update Fail - not exist joboffer")
                    .build();
        }
        JobOffer existJobOffer = jobOfferRepository.getReferenceById(id);
        existJobOffer.setTitle(jobOfferRequest.getTitle());
        existJobOffer.setLocation(jobOfferRequest.getLocation());
        existJobOffer.setEducation(jobOfferRequest.getEducation());
        existJobOffer.setSalaryType(jobOfferRequest.getSalaryType());
        existJobOffer.setSalary(jobOfferRequest.getSalary());
        existJobOffer.setCareer(jobOfferRequest.getCareer());
        existJobOffer.setBody(jobOfferRequest.getBody());
        existJobOffer.setEnvEyesight(jobOfferRequest.getEnvEyesight());
        existJobOffer.setEnvhandWork(jobOfferRequest.getEnvhandWork());
        existJobOffer.setEnvLiftPower(jobOfferRequest.getEnvLiftPower());
        existJobOffer.setEnvStndWalk(jobOfferRequest.getEnvStndWalk());
        existJobOffer.setEnvBothHands(jobOfferRequest.getEnvBothHands());
        existJobOffer.setEnvLstnTalk(jobOfferRequest.getEnvLstnTalk());
        jobOfferRepository.save(existJobOffer);
        return JobOfferResponse.builder()
                .message("Update Success")
                .build();
    }


    @Override
    public void deleteJobOffer(long id) {
        jobOfferRepository.deleteById(id);
    }
}
