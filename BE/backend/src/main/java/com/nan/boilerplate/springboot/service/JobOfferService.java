package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.repository.JobOfferRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.JobOfferSearch;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface JobOfferService {

    List<JobOffer> getJobOfferByCompanyId(Long companyId);
    Page<JobOfferSimpleResponse> getAllJobOffers(Pageable pageable);

    Optional<JobOffer> getJobOfferById(long id);

    Long addJobOffer(JobOfferRequest jobOfferRequest);

    JobOfferSimpleResponse updateJobOffer(Long id, JobOfferRequest jobOfferRequest);

    void deleteJobOffer(long id);

    void getOfficialJobOffers() throws IOException, InterruptedException;

    boolean existsJobOffer(long id);

    Page<JobOfferSimpleResponse> initialJobOffer(Pageable pageable);

    List<JobOffer> searchJobOffer(JobOfferSearch jobOfferSearch);
}
