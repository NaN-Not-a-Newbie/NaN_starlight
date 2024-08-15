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
                .antMatchers("/register/**", "/login/**", "/api/swagger/**", "/actuator/**").permitAll()
                .antMatchers(GET, "/jobOffer/initial").hasAnyAuthority("USER") // 맞춤 공고는 로그인된 유저만
                .antMatchers(GET, "/jobOffer", "/jobOffer/{id}").permitAll() // 공고 조회는 비로그인에게도 허용
                .antMatchers(PATCH, "/admin/manageActive/**").hasAnyAuthority("ADMIN", "STAFF") // 로그인 승인
                .antMatchers(PATCH, "/admin/manageAuthority/**").hasAnyAuthority("ADMIN") // 유저 권한 관리
                // 이력서 작성,수정,삭제는 유저만 허용
                .antMatchers(POST, "/resume").hasAnyAuthority("USER")
                .antMatchers(PUT, "/resume/{id}").hasAnyAuthority("USER")
                .antMatchers(DELETE, "/resume/{id}").hasAnyAuthority("USER")
                
                 // 공고 작성,수정,삭제(관리자도)는 회사만 허용
                .antMatchers(POST, "/jobOffer").hasAnyAuthority("COMPANY")
                .antMatchers(PUT, "/jobOffer/{id}").hasAnyAuthority("COMPANY")
                .antMatchers(DELETE, "/jobOffer/{id}").hasAnyAuthority("COMPANY", "STAFF","ADMIN")

                .antMatchers(PUT, "/memberInfo/company").hasAnyAuthority("COMPANY")
                .antMatchers(PUT, "/memberInfo/user").hasAnyAuthority("USER")

                .antMatchers(POST, "/userApply").hasAnyAuthority("USER") // -> 유저가 기업에게 구직 신청
                .antMatchers(PUT, "/userApply/{id}").hasAnyAuthority("COMPANY") // -> 기업이 유저 채용
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().build();

        //@formatter:on
    }


}
