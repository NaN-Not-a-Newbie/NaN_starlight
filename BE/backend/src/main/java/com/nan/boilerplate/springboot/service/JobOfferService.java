package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;

import java.util.List;
import java.util.Optional;

public interface JobOfferService {
    List<JobOfferResponse> getAllJobOffers();

    Optional<JobOffer> getJobOfferById(long id);

    JobOfferResponse addJobOffer(JobOfferRequest jobOfferRequest);

    void updateJobOffer(Long id, JobOfferRequest jobOfferRequest);

    void deleteJobOffer(long id);
}
