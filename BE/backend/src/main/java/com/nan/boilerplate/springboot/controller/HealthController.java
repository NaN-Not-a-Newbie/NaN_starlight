package com.nan.boilerplate.springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> sayHello() {

        return ResponseEntity.ok("Good!");
    }

}
