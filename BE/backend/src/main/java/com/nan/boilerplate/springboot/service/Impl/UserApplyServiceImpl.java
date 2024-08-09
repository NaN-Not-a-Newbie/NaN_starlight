package com.nan.boilerplate.springboot.service.Impl;


import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.UserApply;
import com.nan.boilerplate.springboot.repository.UserApplyRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import com.nan.boilerplate.springboot.security.dto.UserApplyResponse;
import com.nan.boilerplate.springboot.service.UserApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserApplyServiceImpl implements UserApplyService {
    private final UserApplyRepository userApplyRepository;

    @Autowired
    public UserApplyServiceImpl(UserApplyRepository userApplyRepository) {
        this.userApplyRepository = userApplyRepository;
    }

    @Override
    public List<UserApplyResponse> getAllUserApply() {
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
    public UserApplyResponse addUserApply(UserApplyRequest userApplyRequest) {
        // resume의 user가.. 모르겠음 ㅜㅜ
        return null;
    }

    @Override
    public UserApplyResponse updateUserApply(Long id, UserApplyRequest userApplyRequest) {
        if (userApplyRepository.existsById(id)) {
            UserApply existUserApply = userApplyRepository.getReferenceById(id);
//            existUserApply.setResume();
//            existUserApply.setJobOffer();
//            existUserApply.setHire();
        }
        return null;
    }

    @Override
    public UserApplyResponse deleteUserApplyResponse(Long id) {
        userApplyRepository.deleteById(id);
        return UserApplyResponse.builder().message("Delete Success").build();
    }

}
