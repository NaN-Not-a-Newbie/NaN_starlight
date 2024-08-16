package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.Resume;
import com.nan.boilerplate.springboot.security.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ResumeService {

    Page<ResumeSimpleResponse> getAllResumes(Pageable pageable);

    Optional<Resume> getResumeById(Long id);

    Long addResume(ResumeRequest resumeRequest);

    ResumeSimpleResponse updateResume(Long id, ResumeRequest resumeRequest);

    void deleteResume(Long id);

}
