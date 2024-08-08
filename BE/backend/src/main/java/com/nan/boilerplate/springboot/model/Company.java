package com.nan.boilerplate.springboot.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String CompanyName;

    private String CompanyNum;

    private String PhoneNum;

    private String CompanyAddress;

    private boolean isActive;

    private UserRole Role;

    public boolean isActive(){
        return isActive;
    }
}
