package com.nan.boilerplate.springboot.model;

import javax.persistence.*;

@Entity
public class UserApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="resume_id")
    private Resume resume;

    @ManyToOne
    @JoinColumn(name="joboffer_id")
    private JobOffer jobOffer;

    private boolean hire;
}
