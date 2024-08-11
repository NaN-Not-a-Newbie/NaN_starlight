package com.nan.boilerplate.springboot.service;

import com.nan.boilerplate.springboot.exceptions.RegistrationException;
import com.nan.boilerplate.springboot.repository.CompanyRepository;
import com.nan.boilerplate.springboot.security.dto.CompanyRegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.UserRegistrationRequest;
import com.nan.boilerplate.springboot.repository.UserRepository;
import com.nan.boilerplate.springboot.utils.ExceptionMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService {

    private static final String EMAIL_ALREADY_EXISTS = "email_already_exists";

    private static final String USERNAME_ALREADY_EXISTS = "username_already_exists";

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final ExceptionMessageAccessor exceptionMessageAccessor;

//    public void validateUser(UserRegistrationRequest userRegistrationRequest) {
//
//        final String username = userRegistrationRequest.getUsername();
//
//        checkUsername(username);
//    }
//
//    public void validateCompany(CompanyRegistrationRequest companyRegistrationRequest) {
//
//        final String username = companyRegistrationRequest.getUsername();
//
//        checkUsernameCompany(username);
//    }

    public void validateUsernameUnique(String username) {
        boolean usernameExistsInUsers = userRepository.existsByUsername(username);
        boolean usernameExistsInCompany = companyRepository.existsByUsername(username);

        if (usernameExistsInUsers || usernameExistsInCompany) {
            log.warn("{} is already being used!", username);

            final String existsUsername = exceptionMessageAccessor.getMessage(null, USERNAME_ALREADY_EXISTS);
            throw new RegistrationException(existsUsername);
        }
    }

//    private void checkUsername(String username) {
//
//        final boolean existsByUsername = userRepository.existsByUsername(username);
//
//        if (existsByUsername) {
//
//            log.warn("{} is already being used!", username);
//
//            final String existsUsername = exceptionMessageAccessor.getMessage(null, USERNAME_ALREADY_EXISTS);
//            throw new RegistrationException(existsUsername);
//        }
//
//    }
//
//    private void checkUsernameCompany(String username) {
//
//        final boolean existsByUsername = companyRepository.existsByUsername(username);
//
//        if (existsByUsername) {
//
//            log.warn("{} is already being used!", username);
//
//            final String existsUsername = exceptionMessageAccessor.getMessage(null, USERNAME_ALREADY_EXISTS);
//            throw new RegistrationException(existsUsername);
//        }
//
//    }

}
