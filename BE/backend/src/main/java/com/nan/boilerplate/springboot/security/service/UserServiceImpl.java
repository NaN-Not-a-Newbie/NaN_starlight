package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.model.UserRole;
import com.nan.boilerplate.springboot.repository.CompanyRepository;
import com.nan.boilerplate.springboot.repository.UserRepository;
import com.nan.boilerplate.springboot.security.dto.*;
import com.nan.boilerplate.springboot.security.mapper.UserMapper;
import com.nan.boilerplate.springboot.security.utils.SecurityConstants;
import com.nan.boilerplate.springboot.service.UserValidationService;
import com.nan.boilerplate.springboot.utils.GeneralMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserValidationService userValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public RegistrationResponse registrationUser(UserRegistrationRequest registrationRequest) {

        userValidationService.validateUser(registrationRequest); // 이미 존재하는 유저인지 확인

        final User user = UserMapper.INSTANCE.convertToUser(registrationRequest);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.USER);
        user.setActive(false); // 가입시 isActive를 false로 설정 -> 증명자료 확인 후 true

        userRepository.save(user);

        final String username = registrationRequest.getUsername();
        final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

        log.info("{} registered successfully!", username);

        return new RegistrationResponse(registrationSuccessMessage);
    }

    @Override
    public RegistrationResponse registrationCompany(CompanyRegistrationRequest registrationRequest) {

        userValidationService.validateCompany(registrationRequest); // 이미 존재하는 유저인지 확인

        final Company company = UserMapper.INSTANCE.convertToCompany(registrationRequest); // 엔티티 디티오 변환
        company.setPassword(bCryptPasswordEncoder.encode(company.getPassword()));
        company.setRole(UserRole.COMPANY);
        company.setActive(false); // 가입시 isActive를 false로 설정 -> 증명자료 확인 후 true

        companyRepository.save(company);

        final String username = registrationRequest.getUsername();
        final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

        log.info("{} registered successfully!", username);

        return new RegistrationResponse(registrationSuccessMessage);
    }

    @Override
    public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

        final User user = findByUsername(username);

        return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
    }

    @Override
    public User activateUser(String username) {
        User user = userRepository.findByUsername(username);
        user.setActive(true);
        userRepository.save(user);
        return user;
        }

    @Override
    public User deActivateUser(String username) {
        User user = userRepository.findByUsername(username);
        user.setActive(false);
        userRepository.save(user);
        return user;
    }


    @Override
    public AuthenticatedUserDto demoteUser(String username){
        User user = userRepository.findByUsername(username);
        user.setUserRole(UserRole.USER);
        userRepository.save(user);
        return new AuthenticatedUserDto(username, user.getUserRole(), user.isActive());
    }

    @Override
    public AuthenticatedUserDto promoteUser(String username){
        User user = userRepository.findByUsername(username);
        user.setUserRole(UserRole.STAFF);
        userRepository.save(user);
        return new AuthenticatedUserDto(username, user.getUserRole(), user.isActive());
    }
}

