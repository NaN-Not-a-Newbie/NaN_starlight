package com.nan.boilerplate.springboot.security.service;

import com.nan.boilerplate.springboot.model.Company;
import com.nan.boilerplate.springboot.model.UserRole;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedCompanyDto;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USERNAME_OR_PASSWORD_INVALID = "Invalid username or password.";

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<Company> companyOptional = userService.findByCompanyName(username);

        if (companyOptional.isPresent()) {
            // company
            final AuthenticatedCompanyDto authenticatedCompany = userService.findAuthenticatedCompanyByUsername(username);

            if (Objects.isNull(authenticatedCompany)) {
                throw new UsernameNotFoundException(USERNAME_OR_PASSWORD_INVALID);
            }

            final String authenticatedUsername = authenticatedCompany.getUsername();
            final String authenticatedPassword = authenticatedCompany.getPassword();
            final UserRole userRole = authenticatedCompany.getUserRole();
            final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());

            return new User(authenticatedUsername, authenticatedPassword, Collections.singletonList(grantedAuthority));
        } else {
            // user
            final AuthenticatedUserDto authenticatedUser = userService.findAuthenticatedUserByUsername(username);

            if (Objects.isNull(authenticatedUser)) {
                throw new UsernameNotFoundException(USERNAME_OR_PASSWORD_INVALID);
            }

            final String authenticatedUsername = authenticatedUser.getUsername();
            final String authenticatedPassword = authenticatedUser.getPassword();
            final UserRole userRole = authenticatedUser.getUserRole();
            final SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userRole.name());

            return new User(authenticatedUsername, authenticatedPassword, Collections.singletonList(grantedAuthority));
        }
    }
}
