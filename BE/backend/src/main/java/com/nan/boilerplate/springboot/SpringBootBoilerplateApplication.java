package com.nan.boilerplate.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableAspectJAutoProxy
@EnableJpaAuditing // 생성 시간, 수정 시간 관련 어노테이션
public class SpringBootBoilerplateApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringBootBoilerplateApplication.class, args);
    }

}
