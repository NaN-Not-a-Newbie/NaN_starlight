package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.Resume;
import com.nan.boilerplate.springboot.security.dto.JobOfferRequest;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.ResumeRequest;
import com.nan.boilerplate.springboot.security.dto.ResumeResponse;

import java.util.List;
import java.util.Optional;

public interface ResumeService {

    List<ResumeResponse> getAllResumes();

    Optional<Resume> getResumeById(Long id);

    ResumeResponse addResume(ResumeRequest resumeRequest);

    ResumeResponse updateResume(ResumeRequest resumeRequest);

}
