package com.nan.boilerplate.springboot.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean isActive; // 로그인 시 확인, 탈퇴하면 false로 변경.(기본 false)

    private boolean isWithdraw; // 관리자가 activateUser할 때 확인, true이면 activate 불가

    public boolean isActive() {
        return isActive;
    }

}
