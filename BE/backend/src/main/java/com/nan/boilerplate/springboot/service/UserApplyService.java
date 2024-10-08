package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.model.UserApply;
import com.nan.boilerplate.springboot.security.dto.UserApplyRequest;
import com.nan.boilerplate.springboot.security.dto.UserApplyResponse;

import java.util.List;
import java.util.Optional;

public interface UserApplyService {
    List<UserApplyResponse> getAllUserApply();
    Optional<UserApply> getUserApply(Long id);

    Long addUserApply(UserApplyRequest userApplyRequest);

    UserApplyResponse updateUserApply(Long id, UserApplyRequest userApplyRequest);

    UserApplyResponse deleteUserApplyResponse(Long id);
}
