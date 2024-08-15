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

    private String companyName;

    private String companyRegistrationNumber;

    private String username;

    private String password;

    private String phoneNum;

    private String companyAddress;
    
    private String sighPath; // 서명 저장
    
    private String paperPath; // 계약서

    private UserRole userRole;

    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }
}
