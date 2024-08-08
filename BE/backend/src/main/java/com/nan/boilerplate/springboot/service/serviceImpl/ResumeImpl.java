package com.nan.boilerplate.springboot.service.serviceImpl;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.Resume;
import com.nan.boilerplate.springboot.repository.ResumeRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.ResumeRequest;
import com.nan.boilerplate.springboot.security.dto.ResumeResponse;
import com.nan.boilerplate.springboot.service.ResumeService;
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
public class ResumeImpl implements ResumeService{
    private final ResumeRepository resumeRepository;

    @Override
    public List<ResumeResponse> getAllResumes() {
        List<Resume> resumes = resumeRepository.findAll();
        List<ResumeResponse> resumesResponses = new ArrayList<>();
        for (Resume resume : resumes) {
            resumesResponses.add(ResumeResponse.builder()
                    .body(resume.getBody())
                    .title(resume.getTitle()).build());
        }
        return List.of();
    }

    @Override
    public Optional<Resume> getResumeById(Long id) {
        return Optional.empty();
    }

    @Override
    public ResumeResponse addResume(ResumeRequest resumeRequest) {
        return null;
    }

    @Override
    public ResumeResponse updateResume(ResumeRequest resumeRequest) {
        return null;
    }
}
