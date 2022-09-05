package com.example.harmony.domain.user.controller;

import com.example.harmony.domain.user.dto.SignupRequest;
import com.example.harmony.domain.user.service.KakaoUserService;
import com.example.harmony.domain.user.service.UserService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final KakaoUserService kakaoUserService;

    // 회원가입
    @PostMapping("/api/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, userService.signup(request)));
    }

    // 이메일 중복체크
    @PostMapping("/api/email-check")
    public ResponseEntity<?> emailChk(@RequestBody Map<String,String> map) {
        String msg = "이메일 중복체크 결과를 확인해주세요.";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,msg,userService.emailChk(map.get("email"))));
    }

    // 닉네임 중복체크
    @PostMapping("/api/nickname-check")
    public ResponseEntity<?> nicknameChk(@RequestBody Map<String,String> map) {
        String msg = "닉네임 중복체크 결과를 확인해주세요.";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,msg,userService.nicknameChk(map.get("nickname"))));
    }

    // 가족코드 입력
    @PutMapping("/api/family/join")
    public ResponseEntity<?> enterFamilyCode(@RequestBody Map<String,String> map,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,userService.enterFamilyCode(map.get("familyCode"), userDetails)));
    }

    // 역할 설정
    @PutMapping("/api/user/role")
    public ResponseEntity<?> setRole(@RequestBody Map<String,String> map,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,userService.setRole(map.get("role"), userDetails)));
    }

    // 가족, 역할 설정여부
    @GetMapping("/api/user/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = "가족, 역할 설정여부를 확인해주세요.";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg, userService.getUserInfo(userDetails.getUser())));
    }

    //카카오 소셜 로그인 구현
    @GetMapping("/login/oauth2/kakao")
    public ResponseEntity<?> loginByKakao(@RequestParam String code) throws JsonProcessingException {
        return ResponseEntity.ok().headers(kakaoUserService.loginByKakao(code)).body(new SuccessResponse<>(HttpStatus.OK,"카카오 로그인을 성공하였습니다."));
    }

}