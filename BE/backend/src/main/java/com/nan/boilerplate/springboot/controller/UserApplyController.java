package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.UserApply;
import com.nan.boilerplate.springboot.security.dto.JobOfferSimpleResponse;
import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import com.nan.boilerplate.springboot.security.dto.UserApplyResponse;
import com.nan.boilerplate.springboot.service.FileService;
import com.nan.boilerplate.springboot.service.JobOfferService;
import com.nan.boilerplate.springboot.service.ResumeService;
import com.nan.boilerplate.springboot.service.UserApplyService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.CipherSpi;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/userApply")
public class UserApplyController {
    private final UserApplyService userApplyService;
    private final FileService fileService;
    private final ResumeService resumeService;
    private final JobOfferService jobOfferService;


    @GetMapping
    public ResponseEntity<List<UserApplyResponse>> getAllUserApplies(Pageable pageable) {
        List<UserApplyResponse> jobOfferResponses = userApplyService.getAllUserApply(pageable);
        return ResponseEntity.ok(jobOfferResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserApplyResponse> getUserApplyById(@PathVariable Long id) {
        Optional<UserApply> userApplyOptional = userApplyService.getUserApply(id);
        if (userApplyOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            UserApply userApply = userApplyOptional.get();
            UserApplyResponse response = UserApplyResponse.builder()
                    .jobOfferId(userApply.getJobOffer().getId())
                    .resumeId(userApply.getResume().getId())
                    .hire(userApply.isHire())
                    .message("UsersApply")
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/makeContract")
    public ResponseEntity<Void> makeContract(@RequestBody UserApplyRequest userApplyrequest) {
        fileService.makeContract(userApplyrequest);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).build();
    }

    @GetMapping("/downloadContract")
    public ResponseEntity<Void> downloadContract(HttpServletResponse response) {
        fileService.FileDownloadContract(response);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).build();
    }

    @PostMapping      // User만 허용
    public ResponseEntity<UserApplyResponse> addUserApply(@RequestBody UserApplyRequest request) {
//        Long createdUserApplyId = userApplyService.addUserApply(userApplyrequest);
        if (resumeService.getResumeById(request.getResumeId()).isEmpty()
                || jobOfferService.getJobOfferById(request.getJobOfferId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(UserApplyResponse.toDTO(userApplyService.addUserApply(request)));
//        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
//                .location(URI.create("/userApply/" + createdUserApplyId))
//                .build();  // 리다이렉트
    }

    @PutMapping("/{id}")     // Company만 허용
    public ResponseEntity<UserApplyResponse> updateUserApply(@PathVariable Long id, @RequestBody UserApplyRequest userApplyrequest) {

        return ResponseEntity.ok(userApplyService.updateUserApply(id, userApplyrequest));
    }

    // hire, interview 모두 기본값 false
    // PUT updateUserApply -> setHire(true)     채용
    // PUT updateUserApply -> setHire(false)    미채용
    // PUT updateUserApply -> setInterview(true)    면접 요청
    // PUT updateUserApply -> setInterview(false)   면접 요청 취소(필요할까?)


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserApply(@PathVariable Long id) {
        userApplyService.deleteUserApplyResponse(id);
        return ResponseEntity.noContent().build();
    }
}
