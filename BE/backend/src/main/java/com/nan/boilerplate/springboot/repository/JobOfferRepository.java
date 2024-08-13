package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.JobOffer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    Page<JobOffer> findAll(Pageable pageable);
}
