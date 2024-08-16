package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.exceptions.BadRequestException;
import com.nan.boilerplate.springboot.exceptions.UserNotFoundException;
import com.nan.boilerplate.springboot.model.Resume;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import com.nan.boilerplate.springboot.security.dto.ResumeRequest;
import com.nan.boilerplate.springboot.security.dto.ResumeResponse;
import com.nan.boilerplate.springboot.security.dto.ResumeSimpleResponse;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.PageableValidationService;
import com.nan.boilerplate.springboot.service.ResumeService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/resume")
@AllArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;
    private final PageableValidationService pageableValidationService;

//    @Autowired
//    public ResumeController(ResumeService resumeService) {
//        this.resumeService = resumeService;
//    }

    @GetMapping
    public ResponseEntity<Page<ResumeSimpleResponse>> getAllResumes(Pageable pageable) {
        Pageable pageable1 = pageableValidationService.validateAndCorrectPageable(pageable);

        return ResponseEntity.ok(resumeService.getAllResumes(pageable1));
    }



    // 이력서 자세히 보기
    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResumeById(@PathVariable Long id) {
        System.out.printf("getResumeById({id})1---------\n", id);
        if (resumeService.getResumeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(resumeService.getResumeById(id).get()==null){
            throw new UserNotFoundException("존재하지 않는 유저입니다.");
        }
        Resume resume = resumeService.getResumeById(id).get();
        return ResponseEntity.ok(ResumeResponse.builder()
                .title(resume.getTitle())
                .name(resume.getUser().getName())
                .birthday(resume.getUser().getBirthday())
                .phoneNum(resume.getUser().getPhoneNum())
                .education(resume.getUser().getEducation())
                .body(resume.getBody())
                .build());
    }

    @PostMapping
    public ResponseEntity<ResumeSimpleResponse> addResume(@RequestBody ResumeRequest resumeRequest) {
        Long createdResumeId = resumeService.addResume(resumeRequest);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create("/resume/" + createdResumeId))
                .build();  // 리다이렉트
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResumeSimpleResponse> updateResume(@PathVariable Long id, @RequestBody ResumeRequest resumeRequest) {
        if (resumeService.getResumeById(id).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String myName = SecurityConstants.getAuthenticatedUsername(); // 로그인 된 계정의 username
        String author = resumeService.getResumeById(id).get().getUser().getUsername(); // 글 작성자
        if (!author.equals(myName)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(resumeService.updateResume(id, resumeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResume(@PathVariable Long id) {
        String myName = SecurityConstants.getAuthenticatedUsername();
        if(resumeService.getResumeById(id).get()==null){
            // 로그인 된 계정의 username
            throw new UserNotFoundException("존재하지 않는 유저입니다.");
        }
        String author = resumeService.getResumeById(id).get().getUser().getUsername(); // 글 작성자
        if (!author.equals(myName)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("작성자만 삭제할 수 있습니다.");
        }
        try {
            resumeService.deleteResume(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 이력서입니다. resume id: "+id);
        }
    }
}
