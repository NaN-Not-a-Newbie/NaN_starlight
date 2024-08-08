package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.UserApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserApplyRepository extends JpaRepository<UserApply, Long> {
}
