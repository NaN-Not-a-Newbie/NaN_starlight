package com.nan.boilerplate.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private String age;

    private String signPath; // 서명 저장

    private String paperPath; // 계약서 저장

    private String birthday;

    private String phoneNum;

    private boolean isMale;

    @Enumerated(EnumType.STRING)
    private EnvEyesight envEyesight;

    @Enumerated(EnumType.STRING)
    private EnvBothHands envBothHands;

    @Enumerated(EnumType.STRING)
    private EnvHandWork envhandWork;

    @Enumerated(EnumType.STRING)
    private EnvLiftPower envLiftPower;

    @Enumerated(EnumType.STRING)
    private EnvStndWalk envStndWalk;

    @Enumerated(EnumType.STRING)
    private EnvLstnTalk envLstnTalk;

    @Enumerated(EnumType.STRING)
    private Education education;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private boolean isActive; // 로그인 시 확인, 탈퇴하면 false로 변경.(기본 false)

    private boolean isWithdraw; // 관리자가 activateUser할 때 확인, true이면 activate 불가

//    private LocalDateTime localDateTime=LocalDateTime.now();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public boolean isActive() {
        return isActive;
    }

}
