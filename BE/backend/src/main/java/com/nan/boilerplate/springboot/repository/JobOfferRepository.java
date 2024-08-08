package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
}
