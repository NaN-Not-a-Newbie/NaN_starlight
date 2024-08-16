package com.nan.boilerplate.springboot.service.Impl;

import com.nan.boilerplate.springboot.model.*;
import com.nan.boilerplate.springboot.repository.JobOfferRepository;
import com.nan.boilerplate.springboot.security.dto.JobOfferSearch;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class JobOfferSpecifiction {
    public static Specification<JobOffer> hasSalaryGreaterThan(Long salary) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (salary == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.greaterThan(root.get("salary"), salary);
        };
    }

    public static Specification<JobOffer> likeBody(String body) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (body == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.like(root.get("body"),"%"+ body+"%");
        };
    }
    public static Specification<JobOffer> likeTitle(String title) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (title == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.like(root.get("title"), "%"+title+"%");
        };
    }
    public static Specification<JobOffer> hasCareerLessThan(Long career) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (career == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.lessThan(root.get("career"), career);
        };
    }

    public static Specification<JobOffer> hasEnvBothHands(EnvBothHands envBothHands) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            System.out.println(envBothHands);
            if (envBothHands == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.equal(root.get("envBothHands"), envBothHands);
        };
    }

    public static Specification<JobOffer> hasEnvLiftPower(EnvLiftPower envLiftPower) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (envLiftPower == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.equal(root.get("envLiftPower"), envLiftPower);
        };
    }

    public static Specification<JobOffer> hasEnvLstnTalk(EnvLstnTalk envLstnTalk) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (envLstnTalk == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.equal(root.get("envLstnTalk"), envLstnTalk);
        };
    }

    public static Specification<JobOffer> hasEnvStndWalk(EnvStndWalk envStndWalk) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (envStndWalk ==null) return null; // 조건이 null이면 무시
            return criteriaBuilder.equal(root.get("envStndWalk"), envStndWalk);
        };
    }

    public static Specification<JobOffer> hasEnvHandWork(EnvHandWork envHandWork) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (envHandWork == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.equal(root.get("envhandWork"), envHandWork);
        };
    }

    public static Specification<JobOffer> hasEnvEyesight(EnvEyesight envEyesight) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (envEyesight == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.equal(root.get("envEyesight"), envEyesight);
        };
    }

    public static Specification<JobOffer> hasEducation(Education education) {
        return (Root<JobOffer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (education == null) return null; // 조건이 null이면 무시
            return criteriaBuilder.equal(root.get("education"), education);
        };
    }

    public static List<JobOffer> findJobOffers(JobOfferSearch jobOfferSearch, JobOfferRepository jobOfferRepository) {
        Specification<JobOffer> spec = Specification
                .where(hasSalaryGreaterThan(jobOfferSearch.getSalary()))
                .and(likeBody(jobOfferSearch.getBody()))
                .and(likeTitle(jobOfferSearch.getTitle()))
                .and(hasCareerLessThan(jobOfferSearch.getCareer()))
                .and(hasEnvBothHands(jobOfferSearch.getEnvBothHands()))
                .and(hasEnvLiftPower(jobOfferSearch.getEnvLiftPower()))
                .and(hasEnvLstnTalk(jobOfferSearch.getEnvLstnTalk()))
                .and(hasEnvStndWalk(jobOfferSearch.getEnvStndWalk()))
                .and(hasEnvHandWork(jobOfferSearch.getEnvHandWork()))
                .and(hasEnvEyesight(jobOfferSearch.getEnvEyesight()))
                .and(hasEducation(jobOfferSearch.getEducation()));

        return jobOfferRepository.findAll(spec);
    }

}
