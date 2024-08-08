package com.nan.boilerplate.springboot.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
