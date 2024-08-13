package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.Resume;
import com.nan.boilerplate.springboot.security.dto.ResumeRequest;
import com.nan.boilerplate.springboot.security.dto.ResumeResponse;
import com.nan.boilerplate.springboot.security.dto.ResumeSimpleResponse;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.ResumeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/resume")
public class ResumeController {
    private final ResumeService resumeService;

    @Autowired
    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public ResponseEntity<List<ResumeSimpleResponse>> getAllResumes() {
        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResumeById(@PathVariable Long id) {
        System.out.printf("getResumeById({id})1---------\n", id);
        if (resumeService.getResumeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Resume resume = resumeService.getResumeById(id).get();
        System.out.printf("getResumeById({id})2---------\n", id);
        return ResponseEntity.ok(ResumeResponse.builder()
                .title(resume.getTitle())
                .body(resume.getBody())
                .userName(resume.getUser().getName())
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

        return ResponseEntity.ok(resumeService.updateResume(id, resumeRequest));  // 리다이렉트
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }
}
