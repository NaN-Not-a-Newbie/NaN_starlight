package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.model.UserRole;
import com.nan.boilerplate.springboot.repository.CompanyRepository;
import com.nan.boilerplate.springboot.repository.UserRepository;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.nan.boilerplate.springboot.security.dto.CompanyRegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.RegistrationResponse;
import com.nan.boilerplate.springboot.security.dto.UserRegistrationRequest;
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
public class CompanyServiceImpl implements CompanyService{
    private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

    private final CompanyRepository companyRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserValidationService userValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    @Override
    public Company findByUsername(String username) {

        return companyRepository.findByUsername(username);
    }

    @Override
    public RegistrationResponse registrationCompany(CompanyRegistrationRequest registrationRequest) {

        userValidationService.validateCompany(registrationRequest); // 이미 존재하는 유저인지 확인

        final Company company = UserMapper.INSTANCE.convertToCompany(registrationRequest); // 엔티티 디티오 변환
        company.setPassword(bCryptPasswordEncoder.encode(company.getPassword()));
        company.setRole(UserRole.COMPANY);
//        company.setActive(false); // 가입시 isActive를 false로 설정 -> 증명자료 확인 후 true

        companyRepository.save(company);

        final String username = registrationRequest.getUsername();
        final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

        log.info("{} registered successfully!", username);

        return new RegistrationResponse(registrationSuccessMessage);
    }

    @Override
    public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

        final Company company = findByUsername(username);

        return UserMapper.INSTANCE.convertToAuthenticatedUserDto(company);
    }

    @Override
    public Company activateCompany(String username) {
        Company company = companyRepository.findByUsername(username);
        company.setActive(true);
        companyRepository.save(company);
        return company;
    }

    @Override
    public Company deActivateCompany(String username) {
        Company company = companyRepository.findByUsername(username);
        company.setActive(false);
        companyRepository.save(company);
        return company;
    }

}
