package com.nan.boilerplate.springboot.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;

    private Long salary;

    private String location;

    private Long career;

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

    @Enumerated(EnumType.STRING.STRING)
    private SalaryType salaryType;

    private LocalDateTime created_at=LocalDateTime.now();

}
