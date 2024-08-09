package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.UserRole;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedCompanyDto;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CompanyDetailsServiceImpl implements UserDetailsService {
    private static final String USERNAME_OR_PASSWORD_INVALID = "Invalid username or password.";

    private final CompanyService companyService;

    @Override
    public UserDetails loadUserByUsername(String username) {

        final AuthenticatedCompanyDto authenticatedCommpany = companyService.findAuthenticatedCompanyByCompanyName(username);

        if (Objects.isNull(authenticatedCommpany)) {
            throw new UsernameNotFoundException(USERNAME_OR_PASSWORD_INVALID);
        }

        final String authenticatedUserName = authenticatedCommpany.getUsername();
        final String authenticatedPassword = authenticatedCommpany.getPassword();
        final UserRole userRole = authenticatedCommpany.getUserRole();
        final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());

        return new User(authenticatedUserName, authenticatedPassword, Collections.singletonList(grantedAuthority));
    }

}
