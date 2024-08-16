package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.JobOffer;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nan.boilerplate.springboot.model.*;
import com.nan.boilerplate.springboot.security.dto.JobOfferResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> , JpaSpecificationExecutor<JobOffer> {
    Page<JobOffer> findAll(Pageable pageable);

    List<JobOffer> findByCompanyId(Long companyId);

    Page<JobOffer> findByEnvhandWorkOrEnvBothHandsOrEnvLiftPowerOrEnvLstnTalkOrEnvStndWalkOrEnvEyesight(
            EnvHandWork envHandWork, EnvBothHands envBothHands, EnvLiftPower envLiftPower
            , EnvLstnTalk envLstnTalk, EnvStndWalk envStndWalk, EnvEyesight envEyesight,Pageable pageable);
}
