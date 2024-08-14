package com.nan.boilerplate.springboot.service.Impl;


import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.UserApply;
import com.nan.boilerplate.springboot.repository.JobOfferRepository;
import com.nan.boilerplate.springboot.repository.ResumeRepository;
import com.nan.boilerplate.springboot.repository.UserApplyRepository;
import com.nan.boilerplate.springboot.repository.UserRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import com.nan.boilerplate.springboot.security.dto.UserApplyResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import com.nan.boilerplate.springboot.service.JobOfferService;
import com.nan.boilerplate.springboot.service.UserApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserApplyServiceImpl implements UserApplyService {
    private final UserApplyRepository userApplyRepository;
    private final JobOfferRepository jobOfferRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public List<UserApplyResponse> getAllUserApply(Pageable pageable) {
        List<UserApply> userApplies = userApplyRepository.findAll();
        List<UserApplyResponse> appliesResponses = new ArrayList<>();
        for (UserApply apply : userApplies) {
            appliesResponses.add(UserApplyResponse.builder()
                            .resumeId(apply.getResume().getId())
                            .jobOfferId(apply.getJobOffer().getId())
                            .hire(apply.isHire())
                            .build());
        }
        return appliesResponses;
    }

    @Override
    public Optional<UserApply> getUserApply(Long id) {
        return userApplyRepository.findById(id);
    }

    @Override
    public Long addUserApply(UserApplyRequest userApplyRequest) {
        UserApply userApply = UserApply.builder()
                .jobOffer(jobOfferRepository.findById(userApplyRequest.getJobOfferId()).get())
                .resume(resumeRepository.findById(userApplyRequest.getResumeId()).get())
                .hire(false)
                .build();
        return userApplyRepository.save(userApply).getId();
    }

    @Override
    public UserApplyResponse updateUserApply(Long id, UserApplyRequest userApplyRequest) {
        if (userApplyRepository.existsById(id)) {
            UserApply existUserApply = userApplyRepository.getReferenceById(id);
            if (userApplyRequest.isHire()) {
                existUserApply.setHire(false);
                return UserApplyResponse.builder()
                        .message("Hired").build();
            }
            else{
                existUserApply.setHire(true);
                return UserApplyResponse.builder()
                        .message("Fired").build();
            }
        }
        return null;
    }

    @Override
    public UserApplyResponse deleteUserApplyResponse(Long id) {
        userApplyRepository.deleteById(id);
        return UserApplyResponse.builder().message("Delete Success").build();
    }

}
