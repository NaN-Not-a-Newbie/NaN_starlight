package com.nan.boilerplate.springboot.security.jwt;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.model.UserRole;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedCompanyDto;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.nan.boilerplate.springboot.security.dto.LoginRequest;
import com.nan.boilerplate.springboot.security.dto.LoginResponse;
import com.nan.boilerplate.springboot.security.mapper.UserMapper;
import com.nan.boilerplate.springboot.security.service.CompanyService;
import com.nan.boilerplate.springboot.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final UserService userService;

    private final CompanyService companyService;

    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    // 토큰 생성하는 로직 포함
    public LoginResponse getUserLoginResponse(LoginRequest loginRequest) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~7");

        final String username = loginRequest.getUsername();
        final String password = loginRequest.getPassword();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~8");
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~9");
        authenticationManager.authenticate(usernamePasswordAuthenticationToken); // <-- 순환참조 발생
        System.out.println("~~~~~~~~~~~~~~~~~~~~~10");
        final AuthenticatedUserDto authenticatedUserDto = userService.findAuthenticatedUserByUsername(username);



            final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);
            final String token = jwtTokenManager.generateToken(user);

            log.info("{} has successfully logged in!", user.getUsername());

            return new LoginResponse(token);


    }

    public LoginResponse getCompanyLoginResponse(LoginRequest loginRequest) {
        final String username = loginRequest.getUsername();
        final String password = loginRequest.getPassword();

        final AuthenticatedCompanyDto authenticatedCompanyDto = companyService.findAuthenticatedCompanyByCompanyName(username);

        final Company company = UserMapper.INSTANCE.convertToCompany(authenticatedCompanyDto);
        final String token = jwtTokenManager.generateToken(company);

        log.info("{} has successfully logged in!", company.getUsername());

        return new LoginResponse(token);
    }

}
