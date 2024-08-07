package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.nan.boilerplate.springboot.security.dto.RegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.RegistrationResponse;


public interface UserService {

    User findByUsername(String username);

    User activateUser(String username);

    User deActivateUser(String username);

    RegistrationResponse registration(RegistrationRequest registrationRequest);

    RegistrationResponse withdraw(String password);

    RegistrationResponse withdrawUser(String username);

    AuthenticatedUserDto findAuthenticatedUserByUsername(String username);

    AuthenticatedUserDto demoteUser(String username);

    AuthenticatedUserDto promoteUser(String username);

}
