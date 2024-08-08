package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
