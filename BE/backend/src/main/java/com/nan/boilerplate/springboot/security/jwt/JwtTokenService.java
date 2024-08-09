package com.nan.boilerplate.springboot.security.jwt;

import com.nan.boilerplate.springboot.model.User;
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


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final UserService userService;

    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    public LoginResponse getLoginResponse(LoginRequest loginRequest) {
        System.out.println("2-----------------");
        final String username = loginRequest.getUsername();
        final String password = loginRequest.getPassword();
        System.out.println("21-----------------");
        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        System.out.println("22-----------------");
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        System.out.println("23-----------------");
        final AuthenticatedUserDto authenticatedUserDto = userService.findAuthenticatedUserByUsername(username);
        System.out.println("24-----------------");
        final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);
        System.out.println("25-----------------");
        final String token = jwtTokenManager.generateToken(user);
        System.out.println("26-----------------");
        log.info("{} has successfully logged in!", user.getUsername());

        return new LoginResponse(token);
    }

}
