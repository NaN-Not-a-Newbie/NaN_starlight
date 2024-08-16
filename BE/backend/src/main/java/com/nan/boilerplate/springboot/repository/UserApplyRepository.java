package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.JobOffer;
import com.nan.boilerplate.springboot.model.UserApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserApplyRepository extends JpaRepository<UserApply, Long> {
    Page<UserApply> findAll(Pageable pageable);

    List<UserApply> findByJobOfferId(Long jobOfferId);
}
