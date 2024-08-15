package com.nan.boilerplate.springboot.service.Impl;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.Resume;
import com.nan.boilerplate.springboot.repository.ResumeRepository;
import com.nan.boilerplate.springboot.security.dto.ResumeRequest;
import com.nan.boilerplate.springboot.security.dto.ResumeResponse;
import com.nan.boilerplate.springboot.security.dto.ResumeSimpleResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService{
    private final ResumeRepository resumeRepository;
    private final UserService userService;

    @Override
    public List<ResumeSimpleResponse> getAllResumes() {
        List<Resume> resumes = resumeRepository.findAll();
        List<ResumeSimpleResponse> resumesResponses = new ArrayList<>();
        for (Resume resume : resumes) {
            resumesResponses.add(ResumeSimpleResponse.builder()
                    .id(resume.getId()).title(resume.getTitle()).build());
        }
        return resumesResponses;
    }


    @Override
    public Optional<Resume> getResumeById(Long id) { return resumeRepository.findById(id); }

    // 이력서 작성
    @Override
    public Long addResume(ResumeRequest resumeRequest) {
        Resume resume = Resume.builder()
                .title(resumeRequest.getTitle())
                .body(resumeRequest.getBody())
                .user(userService.findByUsername(SecurityConstants.getAuthenticatedUsername()).get())
                .build();
        return resumeRepository.save(resume).getId();
    }

    // 이력서 수정
    @Override
    public ResumeSimpleResponse updateResume(Long id, ResumeRequest resumeRequest) {

            Resume existResume = resumeRepository.getReferenceById(id);
            existResume.setTitle(resumeRequest.getTitle());
            existResume.setBody(resumeRequest.getBody());
            Resume resume = resumeRepository.save(existResume);
            return ResumeSimpleResponse.builder()
                    .id(resume.getId())
                    .title(resume.getTitle()).build();

    }

    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }
}
