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

import javax.crypto.spec.OAEPParameterSpec;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

    private static final String WITHDRAW_SUCCESSFUL = "withdraw_successful";

    private static final String WRONG_PASSWORD = "wrong_password";

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserValidationService userValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<Company> findByCompanyName(String username) {
        return companyRepository.findByUsername(username);
    }


    @Override
    public RegistrationResponse registrationUser(UserRegistrationRequest userRegistrationRequest) {
        userValidationService.validateUsernameUnique(userRegistrationRequest.getUsername()); // 이미 존재하는 유저인지 확인
        userValidationService.checkPassword(userRegistrationRequest.getPassword(), userRegistrationRequest.getPassword2());

        final User user = UserMapper.INSTANCE.convertToUser(userRegistrationRequest); // 엔티티 디티오 변환
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUserRole(UserRole.USER);
        user.setAge(calculateAge(userRegistrationRequest.getBirthday()));
        user.setActive(true); // 가입시 isActive를 false로 설정

        userRepository.save(user);

        final String username = userRegistrationRequest.getUsername();
        final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

        log.info("{} registered successfully!", username);

        return new RegistrationResponse().builder().message(registrationSuccessMessage).username(username).password(userRegistrationRequest.getPassword()).build();//User

    }

    @Override
    public RegistrationResponse registrationCompany(CompanyRegistrationRequest companyRegistrationRequest) {

        userValidationService.validateUsernameUnique(companyRegistrationRequest.getUsername()); // 이미 존재하는 유저인지 확인
        userValidationService.checkPassword(companyRegistrationRequest.getPassword(), companyRegistrationRequest.getPassword2());

            final Company company = UserMapper.INSTANCE.convertToCompany(companyRegistrationRequest); // 엔티티 디티오 변환
            company.setPassword(bCryptPasswordEncoder.encode(company.getPassword()));
            company.setUserRole(UserRole.COMPANY);
            company.setActive(true); // 가입시 isActive를 false로 설정

            companyRepository.save(company);

            final String companyName = companyRegistrationRequest.getCompanyName();
            final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, companyName);

            log.info("{} registered successfully!", companyName);

            return new RegistrationResponse().builder().message(registrationSuccessMessage).username(companyName).password(companyRegistrationRequest.getPassword()).build();//User
    }

    @Override
    public AuthenticatedCompanyDto findAuthenticatedCompanyByUsername(String username) {
        final Company company = findByCompanyName(username).get();

        return UserMapper.INSTANCE.convertToAuthenticatedCompanyDto(company);
    }

    @Override
    public AuthenticatedUserDto findAuthenticatedUserByUsername(String username) {

        final User user = findByUsername(username).get();

        return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
    }

    @Override
    public User activateUser(String username) {
        User user = userRepository.findByUsername(username).get();
        user.setActive(true);
        userRepository.save(user);
        return user;
        }

    @Override
    public User deActivateUser(String username) {
        User user = userRepository.findByUsername(username).get();
        user.setActive(false);
        userRepository.save(user);
        return user;
    }

    @Override
    public Company activateCompany(String username) {
        Company company = companyRepository.findByUsername(username).get();
        company.setActive(false);
        companyRepository.save(company);
        return company;
    }

    @Override
    public Company deActivateCompany(String username) {
        Company company = companyRepository.findByUsername(username).get();
        company.setActive(false);
        companyRepository.save(company);
        return company;
    }
    @Override
    public AuthenticatedUserDto demoteUser(String username){
        User user = userRepository.findByUsername(username).get();
        user.setUserRole(UserRole.USER);
        userRepository.save(user);
        return new AuthenticatedUserDto(username, user.getUserRole(), user.isActive());
    }

    @Override
    public AuthenticatedUserDto promoteUser(String username){
        User user = userRepository.findByUsername(username).get();
        user.setUserRole(UserRole.STAFF);
        userRepository.save(user);
        return new AuthenticatedUserDto(username, user.getUserRole(), user.isActive());
    }


    private int calculateAge(String birthday) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(birthday, formatter);
        LocalDate currentDate = LocalDate.now();

        int age = Period.between(birthDate, currentDate).getYears();

        // 생일이 지났는지
        if (currentDate.getDayOfYear() < birthDate.getDayOfYear()) {
            age -= 1;
        }

        return age;
    }

}

