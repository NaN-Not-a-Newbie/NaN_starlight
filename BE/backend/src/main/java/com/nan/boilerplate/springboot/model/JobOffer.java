package com.nan.boilerplate.springboot.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;

    private Long salary;

    private String location;

    private Long career;

    private String deadLine;

    @ManyToOne
    @JoinColumn(name="company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    private EnvEyesight envEyesight;

    @Enumerated(EnumType.STRING)
    private EnvBothHands envBothHands;

    @Enumerated(EnumType.STRING)
    private EnvHandWork envhandWork;

    @Enumerated(EnumType.STRING)
    private EnvLiftPower envLiftPower;

    @Enumerated(EnumType.STRING)
    private EnvStndWalk envStndWalk;

    @Enumerated(EnumType.STRING)
    private EnvLstnTalk envLstnTalk;

    @Enumerated(EnumType.STRING)
    private Education education;

    @Enumerated(EnumType.STRING)
    private SalaryType salaryType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
