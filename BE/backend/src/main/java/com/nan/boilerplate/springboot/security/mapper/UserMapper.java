package com.nan.boilerplate.springboot.security.mapper;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.nan.boilerplate.springboot.security.dto.CompanyRegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.RegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.UserRegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User convertToUser(UserRegistrationRequest registrationRequest);

    Company convertToCompany(CompanyRegistrationRequest registrationRequest);

    AuthenticatedUserDto convertToAuthenticatedUserDto(User user);

    AuthenticatedUserDto convertToAuthenticatedUserDto(Company company);

    User convertToUser(AuthenticatedUserDto authenticatedUserDto);

}
