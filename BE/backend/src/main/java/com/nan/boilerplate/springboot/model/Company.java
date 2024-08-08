package com.nan.boilerplate.springboot.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CompanyName;

    private Long companyRegistrationNumber;

    private String username;

    private String password;

    private String PhoneNum;

    private String CompanyAddress;

    private UserRole role;

    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }
}
