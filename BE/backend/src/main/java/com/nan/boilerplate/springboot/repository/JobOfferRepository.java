package com.nan.boilerplate.springboot.repository;

import com.nan.boilerplate.springboot.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
    Page<JobOffer> findAll(Pageable pageable);

    Page<JobOffer> findByEnvhandWorkOrEnvBothHandsOrEnvEyesightOrEnvLiftPowerOrEnvLstnTalkOrEnvStndWalk(
            EnvHandWork envhandWork,
            EnvBothHands envBothHands,
            EnvEyesight envEyesight,
            EnvLiftPower envLiftPower,
            EnvLstnTalk envLstnTalk,
            EnvStndWalk envStndWalk,
            Pageable pageable
    );

}


