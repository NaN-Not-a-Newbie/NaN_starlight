package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.*;


public interface UserService {

    User findByUsername(String username);

    User activateUser(String username);

    User deActivateUser(String username);

    RegistrationResponse registrationUser(UserRegistrationRequest registrationRequest);

    RegistrationResponse registrationCompany(CompanyRegistrationRequest registrationRequest);

    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

    AuthenticatedUserDto demoteUser(String username);

    AuthenticatedUserDto promoteUser(String username);

}
