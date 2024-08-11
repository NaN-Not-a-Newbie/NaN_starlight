package com.nan.boilerplate.springboot.configuration;

import com.nan.boilerplate.springboot.security.jwt.JwtAuthenticationEntryPoint;
import com.nan.boilerplate.springboot.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //@formatter:off

        return http.cors().and().csrf().disable()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/register/**", "/health", "/login/**", "/api/swagger/**", "/actuator/**").permitAll()
                .antMatchers(PATCH, "/admin/manageActive/**").hasAnyAuthority("ADMIN", "STAFF") // 로그인 승인
                .antMatchers(PATCH, "/admin/manageAuthority/**").hasAnyAuthority("ADMIN")
//                .antMatchers(POST, "/resume").hasAnyAuthority("USER") // 공고 작성과 수정은 회사만,
//                .antMatchers(POST, "/jobOffer").hasAnyAuthority("COMPANY") // 지원서 작성과 수정은 유저만 허용
//                .antMatchers(PUT, "/resume/{id}").hasAnyAuthority("USER")
//                .antMatchers(PUT, "/jobOffer/{id}").hasAnyAuthority("COMPANY")
                .antMatchers(POST, "/userApply").hasAnyAuthority("USER")
                .antMatchers(PUT, "/userApply/{id}").hasAnyAuthority("COMPANY")
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().build();

        //@formatter:on
    }


}
