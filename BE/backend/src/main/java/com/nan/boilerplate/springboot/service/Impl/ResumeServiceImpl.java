package com.nan.boilerplate.springboot.service.Impl;

import com.nan.boilerplate.springboot.exceptions.UserNotFoundException;
import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.Resume;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.repository.ResumeRepository;
import com.nan.boilerplate.springboot.repository.UserRepository;
import com.nan.boilerplate.springboot.security.dto.ResumeRequest;
import com.nan.boilerplate.springboot.security.dto.ResumeResponse;
import com.nan.boilerplate.springboot.security.dto.ResumeSimpleResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.ResumeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final UserRepository userRepository;

    @Override
    public Page<ResumeSimpleResponse> getAllResumes(Pageable pageable) {
        String myName = SecurityConstants.getAuthenticatedUsername();
        Optional<User> userOptional = userService.findByUsername(myName);
        if (userOptional.isEmpty()) {

        }
        if(userService.findByUsername(myName).get()==null){
            throw new UserNotFoundException("존재하지 않는 유저입니다.");
        }
        Long myId = userService.findByUsername(myName).get().getId();
        return resumeRepository.findByUserId(myId, pageable).map(ResumeSimpleResponse::toDTO);

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
