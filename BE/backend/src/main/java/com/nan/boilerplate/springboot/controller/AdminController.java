package com.nan.boilerplate.springboot.controller;

import com.nan.boilerplate.springboot.exceptions.UserNotFoundException;
import com.nan.boilerplate.springboot.model.User;
import com.nan.boilerplate.springboot.security.dto.AuthenticatedUserDto;
import com.nan.boilerplate.springboot.security.dto.RegistrationResponse;
import com.nan.boilerplate.springboot.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    // 회원가입 승인
    @PatchMapping("/manageActive/activateUser")
    public ResponseEntity<AuthenticatedUserDto> activateUser(@RequestParam String username) {
        try {
            User user = userService.activateUser(username);
            return ResponseEntity.ok(new AuthenticatedUserDto(user.getUsername(), user.getUserRole(), user.isActive()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/manageActive/deActivateUser")
    public ResponseEntity<AuthenticatedUserDto> deActivateUser(@RequestParam String username) {
        try {
            User user = userService.deActivateUser(username);
            return ResponseEntity.ok(new AuthenticatedUserDto(user.getUsername(), user.getUserRole(), user.isActive()));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 탈퇴시키기
    @PatchMapping("/manageActive/withdrawUser")
    public ResponseEntity<RegistrationResponse> withdrawUser(@RequestParam String username) {
        try {

            RegistrationResponse response = userService.withdrawUser(username);
            if (response.getMessage().isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            } else {
                return ResponseEntity.ok(response);
            }

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 권한 관리
    @PatchMapping("/manageAuthority/demoteUser")
    public ResponseEntity<AuthenticatedUserDto> demoteUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.demoteUser(username));
    }

    @PatchMapping("/manageAuthority/promoteUser")
    public ResponseEntity<AuthenticatedUserDto> promoteUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.promoteUser(username));
    }
}

