package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.*;

import java.util.Optional;


public interface UserService {

    Optional<User> findByUsername(String username);

    User activateUser(String username);

    User deActivateUser(String username);

    RegistrationResponse registrationUser(UserRegistrationRequest userRegistrationRequest);

    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);



    AuthenticatedUserDto demoteUser(String username);

    AuthenticatedUserDto promoteUser(String username);

    Optional<Company> findByCompanyName(String username);

    Company activateCompany(String username);

    Company deActivateCompany(String username);

    RegistrationResponse registrationCompany(CompanyRegistrationRequest userRegistrationRequest);

    AuthenticatedCompanyDto findAuthenticatedCompanyByUsername(String username);

//    String calculateAge(String birthday);

}
