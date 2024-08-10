package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.Resume;
import com.nan.boilerplate.springboot.security.dto.ResumeRequest;
import com.nan.boilerplate.springboot.security.dto.ResumeResponse;
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
    public ResponseEntity<List<ResumeResponse>> getAllResumes() {
        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> getResumeById(@PathVariable Long id) {
        if (resumeService.getResumeById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            Resume resume = resumeService.getResumeById(id).get();
            return ResponseEntity.ok(ResumeResponse.builder()
                            .title(resume.getTitle())
                            .body(resume.getBody())
                            .user(resume.getUser())
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<Void> addResume(@RequestBody ResumeRequest resumeRequest) {
        Long createdresumeId = resumeService.addResume(resumeRequest);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create("/resume/" + createdresumeId))
                .build();  // 리다이렉트
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResumeResponse> updateResume(@PathVariable Long id, @RequestBody ResumeRequest resumeRequest) {
        return ResponseEntity.ok(resumeService.updateResume(id, resumeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResumeResponse> deleteResume(@PathVariable Long id) {
        return ResponseEntity.ok(resumeService.deleteResume(id));
    }
}
