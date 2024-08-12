package com.nan.boilerplate.springboot.security.mapper;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedCompanyDto;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.nan.boilerplate.springboot.security.dto.CompanyRegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.UserRegistrationRequest;

public class UserMapperImpl2 implements UserMapper{
    public UserMapperImpl2() {
    }

    public User convertToUser(UserRegistrationRequest userRegistrationRequest) {
        if (userRegistrationRequest == null) {
            return null;
        } else {
            User.UserBuilder user = User.builder();
            user.name(userRegistrationRequest.getName());
            user.username(userRegistrationRequest.getUsername());
            user.password(userRegistrationRequest.getPassword());
            user.birthday(userRegistrationRequest.getBirthday());
            user.phoneNum(userRegistrationRequest.getPhoneNum());
            user.envEyesight(userRegistrationRequest.getEnvEyesight());
            user.envBothHands(userRegistrationRequest.getEnvBothHands());
            user.envhandWork(userRegistrationRequest.getEnvhandWork());
            user.envLiftPower(userRegistrationRequest.getEnvLiftPower());
            user.envStndWalk(userRegistrationRequest.getEnvStndWalk());
            user.envLstnTalk(userRegistrationRequest.getEnvLstnTalk());
            user.education(userRegistrationRequest.getEducation());
            return user.build();
        }
    }

    public Company convertToCompany(CompanyRegistrationRequest companyRegistrationRequest) {
        if (companyRegistrationRequest == null) {
            return null;
        } else {
            Company.CompanyBuilder company = Company.builder();
            company.companyName(companyRegistrationRequest.getCompanyName());
            company.companyRegistrationNumber(companyRegistrationRequest.getCompanyRegistrationNumber());
            company.username(companyRegistrationRequest.getUsername());
            company.password(companyRegistrationRequest.getPassword());
            company.phoneNum(companyRegistrationRequest.getPhoneNum());
            company.companyAddress(companyRegistrationRequest.getCompanyAddress());
            return company.build();
        }
    }

    public AuthenticatedUserDto convertToAuthenticatedUserDto(User user) {
        if (user == null) {
            return null;
        } else {
            AuthenticatedUserDto authenticatedUserDto = new AuthenticatedUserDto();
            authenticatedUserDto.setName(user.getName());
            authenticatedUserDto.setUsername(user.getUsername());
            authenticatedUserDto.setPassword(user.getPassword());
            authenticatedUserDto.setUserRole(user.getUserRole());
            authenticatedUserDto.setActive(user.isActive());
            return authenticatedUserDto;
        }
    }

    public AuthenticatedCompanyDto convertToAuthenticatedCompanyDto(Company company) {
        if (company == null) {
            return null;
        } else {
            AuthenticatedCompanyDto authenticatedCompanyDto = new AuthenticatedCompanyDto();
            authenticatedCompanyDto.setUsername(company.getUsername());
            authenticatedCompanyDto.setPassword(company.getPassword());
            authenticatedCompanyDto.setUserRole(company.getUserRole());
            authenticatedCompanyDto.setActive(company.isActive());
            return authenticatedCompanyDto;
        }
    }

    public User convertToUser(AuthenticatedUserDto authenticatedUserDto) {
        if (authenticatedUserDto == null) {
            return null;
        } else {
            User.UserBuilder user = User.builder();
            user.name(authenticatedUserDto.getName());
            user.username(authenticatedUserDto.getUsername());
            user.password(authenticatedUserDto.getPassword());
            user.userRole(authenticatedUserDto.getUserRole());
            return user.build();
        }
    }

    public Company convertToCompany(AuthenticatedCompanyDto authenticatedCompanyDto) {
        if (authenticatedCompanyDto == null) {
            return null;
        } else {
            Company.CompanyBuilder company = Company.builder();
            company.username(authenticatedCompanyDto.getUsername());
            company.password(authenticatedCompanyDto.getPassword());
            company.userRole(authenticatedCompanyDto.getUserRole());
            return company.build();
        }
    }
}
