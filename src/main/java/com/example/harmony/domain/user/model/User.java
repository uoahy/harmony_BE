package com.example.harmony.domain.user.model;

import com.example.harmony.domain.user.dto.UpdateInfoRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Family family;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    // 카카오 로그인 구현시 닉네임 추가입력 필요
    @Column(unique = true)
    private String nickname;

    // 카카오 사용자의 경우 password 값 null
    @Column
    private String password;

    @Column
    private String gender;

    @Enumerated(value = EnumType.STRING)
    private RoleEnum role;

    public User(String email, String name, String nickname, String password, String gender) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.password = password;
        this.gender = gender;
        this.role = RoleEnum.NOBODY;
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
        this.role = RoleEnum.NOBODY;
    }

    public void setFamily(Family family) {
        // 가족 중복체크
        if(this.family!=null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 가족이 등록된 유저입니다.");
        }
        this.family = family;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public void updateMyPage(String nickname) { this.nickname = nickname; }

    public void updateKakao(UpdateInfoRequest request) {
        this.nickname = request.getNickname();
        this.gender = request.getGender();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}