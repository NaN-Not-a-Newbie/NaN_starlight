package com.nan.boilerplate.springboot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private LocalDateTime created_at=LocalDateTime.now();
}
