package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.model.UserRole;
import com.nan.boilerplate.springboot.repository.CompanyRepository;
import com.nan.boilerplate.springboot.repository.UserRepository;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.nan.boilerplate.springboot.security.dto.RegistrationRequest;
import com.nan.boilerplate.springboot.security.dto.RegistrationResponse;
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

    private static final String WITHDRAW_SUCCESSFUL = "withdraw_successful";

    private static final String WRONG_PASSWORD = "wrong_password";

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserValidationService userValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;
    private final CompanyRepository companyRepository;

    @Override
    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public RegistrationResponse registration(RegistrationRequest registrationRequest) {

        if(registrationRequest.getRegistCode()==0){
            userValidationService.validateUser(registrationRequest); // 이미 존재하는 유저인지 확인

            final User user = UserMapper.INSTANCE.convertToUser(registrationRequest); // 엔티티 디티오 변환
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setUserRole(UserRole.USER);
            user.setActive(false); // 가입시 isActive를 false로 설정

            userRepository.save(user);

            final String username = registrationRequest.getUsername();
            final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

            log.info("{} registered successfully!", username);

            return new RegistrationResponse(registrationSuccessMessage);//User
        }
        else if(registrationRequest.getRegistCode()==1){
            userValidationService.validateUser(registrationRequest); // 이미 존재하는 유저인지 확인

            final User user = UserMapper.INSTANCE.convertToUser(registrationRequest); // 엔티티 디티오 변환
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setUserRole(UserRole.COMPANY);
            user.setActive(false); // 가입시 isActive를 false로 설정

            userRepository.save(user);

            final String username = registrationRequest.getUsername();
            final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, username);

            log.info("{} registered successfully!", username);

            return new RegistrationResponse(registrationSuccessMessage);//User
        }
        return null;
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
    public RegistrationResponse withdraw(String password) {
        String username = SecurityConstants.getAuthenticatedUsername();
        User user = userRepository.findByUsername(username);
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            user.setActive(false);
            user.setWithdraw(true);
            userRepository.save(user);
            return new RegistrationResponse(user+WITHDRAW_SUCCESSFUL);
        } else {
            return new RegistrationResponse(WRONG_PASSWORD);
        }
    }

    // 탈퇴시키기
    @Override
    public RegistrationResponse withdrawUser(String username) {
        String myName = SecurityConstants.getAuthenticatedUsername();
        UserRole myRole = userRepository.findByUsername(myName).getUserRole();
        User user = userRepository.findByUsername(username); // 탈퇴시킬 유저
        if (myRole == UserRole.ADMIN) {
            user.setActive(false);
            user.setWithdraw(true);
            userRepository.save(user);
            return new RegistrationResponse(username+WITHDRAW_SUCCESSFUL);
        } else {
            if (user.getUserRole()==UserRole.USER) {
                user.setActive(false);
                user.setWithdraw(true);
                userRepository.save(user);
                return new RegistrationResponse(username+WITHDRAW_SUCCESSFUL);
            }
            return new RegistrationResponse(""); // 권한없음
        }
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

