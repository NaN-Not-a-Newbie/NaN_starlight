package com.nan.boilerplate.springboot.service.Impl;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.repository.CompanyRepository;
import com.nan.boilerplate.springboot.repository.JobOfferRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobOfferServiceImpl implements JobOfferService {
    private final JobOfferRepository jobOfferRepository;
    private final CompanyRepository companyRepository;

    @Override
    public List<JobOfferSimpleResponse> getAllJobOffers() {
        List<JobOffer> offers = jobOfferRepository.findAll();
//        List<JobOfferSimpleResponse> offersResponses = new ArrayList<>();
//        for (JobOffer offer : offers) {
//            offersResponses.add(JobOfferSimpleResponse.builder()
//                    .id(offer.getId())
//                    .companyName(offer.getCompany().getCompanyName())
//                    .title(offer.getTitle())
//                    .salaryType(offer.getSalaryType())
//                    .salary(offer.getSalary())
//                    .career(offer.getSalary())
//                    .education(offer.getEducation())
//                    .envEyesight(offer.getEnvEyesight())
//                    .envBothHands(offer.getEnvBothHands())
//                    .envhandWork(offer.getEnvhandWork())
//                    .envLiftPower(offer.getEnvLiftPower())
//                    .envStndWalk(offer.getEnvStndWalk())
//                    .envLstnTalk(offer.getEnvLstnTalk())
//                    .deadLine(offer.getDeadLine())
//                    .build());
//        }
        return offers.stream().map(JobOfferSimpleResponse::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<JobOfferSimpleResponse> getAllJobOffersPage(Pageable pageable) {
        List<JobOffer> jobOffers = jobOfferRepository.findAll(pageable).getContent();

        return jobOffers.stream().map(JobOfferSimpleResponse::toDTO).collect(Collectors.toList());
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
                .company(companyRepository.findByUsername(SecurityConstants.getAuthenticatedUsername()).get())
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

        return jobOfferRepository.save(jobOffer).getId();
    }

    @Override
    public JobOfferSimpleResponse updateJobOffer(Long id, JobOfferRequest jobOfferRequest) {
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
        existJobOffer.setDeadLine(jobOfferRequest.getDeadLine());

        JobOffer updetedJobOffer = jobOfferRepository.save(existJobOffer);
        return JobOfferSimpleResponse.builder()
                .title(updetedJobOffer.getTitle())
                .companyName(updetedJobOffer.getCompany().getCompanyName())
                .build();


    }

    @Override
    public void deleteJobOffer(long id) {
        jobOfferRepository.deleteById(id);
    }

}
