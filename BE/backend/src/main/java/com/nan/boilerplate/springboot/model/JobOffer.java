package com.nan.boilerplate.springboot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    private Education education;

    private LocalDateTime created_at=LocalDateTime.now();


}
