package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;

import java.util.List;
import java.util.Optional;

public interface JobOfferService {
    List<JobOfferSimpleResponse> getAllJobOffers();

    Optional<JobOffer> getJobOfferById(long id);

    Long addJobOffer(JobOfferRequest jobOfferRequest);
//    JobOfferResponse addJobOffer(JobOfferRequest jobOfferRequest);

    JobOfferResponse updateJobOffer(Long id, JobOfferRequest jobOfferRequest);

    void deleteJobOffer(long id);
}
