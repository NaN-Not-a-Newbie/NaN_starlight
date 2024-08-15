package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.model.UserApply;
import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import com.nan.boilerplate.springboot.security.dto.UserApplyResponse;
import com.nan.boilerplate.springboot.service.FileService;
import com.nan.boilerplate.springboot.service.UserApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/userApply")
public class UserApplyController {
    private final UserApplyService userApplyService;
    private final FileService fileService;
    @GetMapping
    public ResponseEntity<List<UserApplyResponse>> getAllUserApplies(Pageable pageable) {
        List<UserApplyResponse> jobOfferResponses = userApplyService.getAllUserApply(pageable);
        return ResponseEntity.ok(jobOfferResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserApplyResponse> getUserApplyById(@PathVariable Long id) {
        if (userApplyService.getUserApply(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            UserApply userApply = userApplyService.getUserApply(id).get();
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

    @PostMapping
    public ResponseEntity<Void> addUserApply(@RequestBody UserApplyRequest userApplyrequest) {
        Long createdUserApplyId = userApplyService.addUserApply(userApplyrequest);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .location(URI.create("/userApply/" + createdUserApplyId))
                .build();  // 리다이렉트
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserApplyResponse> updateUserApply(@PathVariable Long id, @RequestBody UserApplyRequest userApplyrequest) {
        return ResponseEntity.ok(userApplyService.updateUserApply(id, userApplyrequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserApply(@PathVariable Long id) {
        userApplyService.deleteUserApplyResponse(id);
        return ResponseEntity.noContent().build();
    }
}
