package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface JobOfferService {
    List<JobOfferSimpleResponse> getAllJobOffers();

    List<JobOfferSimpleResponse> getAllJobOffersPage(Pageable pageable);

    Optional<JobOffer> getJobOfferById(long id);

    Long addJobOffer(JobOfferRequest jobOfferRequest);
//    JobOfferResponse addJobOffer(JobOfferRequest jobOfferRequest);

    JobOfferSimpleResponse updateJobOffer(Long id, JobOfferRequest jobOfferRequest);

    void deleteJobOffer(long id);

}
