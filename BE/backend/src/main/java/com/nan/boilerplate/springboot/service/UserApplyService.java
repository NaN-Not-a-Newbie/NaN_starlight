package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.UserApply;
import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import com.nan.boilerplate.springboot.security.dto.UserApplyResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserApplyService {

//    List<JobOffer> getJobOfferByCompanyId(Long companyId);
//
//    UserApply findByJobOfferId(Long jobOfferId);

    List<UserApplyResponse> getAllUserApply(Pageable pageable);
    Optional<UserApply> getUserApply(Long id);

    UserApply addUserApply(UserApplyRequest userApplyRequest);

    UserApplyResponse updateUserApply(Long id, UserApplyRequest userApplyRequest);

    UserApplyResponse deleteUserApplyResponse(Long id);
}
