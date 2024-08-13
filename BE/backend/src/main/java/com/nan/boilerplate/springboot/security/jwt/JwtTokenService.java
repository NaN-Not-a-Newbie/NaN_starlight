package com.nan.boilerplate.springboot.security.jwt;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.repository.UserRepository;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedCompanyDto;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.nan.boilerplate.springboot.security.dto.LoginRequest;
import com.nan.boilerplate.springboot.security.dto.LoginResponse;
import com.nan.boilerplate.springboot.security.mapper.UserMapper;
import com.nan.boilerplate.springboot.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final UserService userService;

    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    public LoginResponse getLoginResponse(LoginRequest loginRequest) {
        System.out.println("----------10");
        final String username = loginRequest.getUsername();
        final String password = loginRequest.getPassword();
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        System.out.println("----------20");
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println("----------30");
        if(userService.findByUsername(loginRequest.getUsername())==null){
            final AuthenticatedCompanyDto authenticatedCompanyDto = userService.findAuthenticatedCompanyByUsername(username);
            final Company user = UserMapper.INSTANCE.convertToCompany(authenticatedCompanyDto);
            final String token = jwtTokenManager.generateCompanyToken(user);
            log.info("{} has successfully logged in!", user.getUsername());
            return new LoginResponse(token,"");
        }
        else{
            final AuthenticatedUserDto authenticatedUserDto = userService.findAuthenticatedUserByUsername(username);
            final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);
            final String token = jwtTokenManager.generateToken(user);
            log.info("{} has successfully logged in!", user.getUsername());
            return new LoginResponse(token,"");
        }
    }

}
