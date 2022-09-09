package com.example.harmony.domain.user.controller;

import com.example.harmony.domain.user.dto.SignupRequest;
import com.example.harmony.domain.user.dto.UpdateInfoRequest;
import com.example.harmony.domain.user.dto.UpdatePasswordRequest;
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
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, userService.signup(request)));
    }

    // 이메일 중복체크
    @PostMapping("/email-check")
    public ResponseEntity<?> emailChk(@RequestBody Map<String,String> map) {
        String msg = "이메일 중복체크 결과를 확인해주세요.";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,msg,userService.emailChk(map.get("email"))));
    }

    // 닉네임 중복체크
    @PostMapping("/nickname-check")
    public ResponseEntity<?> nicknameChk(@RequestBody Map<String,String> map) {
        String msg = "닉네임 중복체크 결과를 확인해주세요.";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,msg,userService.nicknameChk(map.get("nickname"))));
    }

    // 가족코드 입력
    @PutMapping("/family/join")
    public ResponseEntity<?> enterFamilyCode(@RequestBody Map<String,String> map,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,userService.enterFamilyCode(map.get("familyCode"), userDetails)));
    }

    // 역할 설정
    @PutMapping("/user/role")
    public ResponseEntity<?> setRole(@RequestBody Map<String,String> map,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,userService.setRole(map.get("role"), userDetails)));
    }

    // 가족, 역할 설정여부
    @GetMapping("/user/info")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = "가족, 역할 설정여부를 확인해주세요.";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg, userService.getUserInfo(userDetails.getUser())));
    }

    //카카오 소셜 로그인 구현
    @GetMapping("/login/oauth2/kakao")
    public ResponseEntity<?> loginByKakao(@RequestParam String code) throws JsonProcessingException {
        return ResponseEntity.ok().headers(kakaoUserService.loginByKakao(code)).body(new SuccessResponse<>(HttpStatus.OK,"카카오 로그인을 성공하였습니다."));
    }

    // 회원탈퇴
    @DeleteMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(@RequestBody Map<String, String> password,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, userService.withdrawal(password.get("password"), userDetails.getUser())));
    }

    // 탈퇴고객 피드백
    @PostMapping("/withdrawal")
    public ResponseEntity<?> getFeedback(@RequestBody Map<String, String> feedback) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, userService.getFeedback(feedback.get("feedback")) ));
    }

    // 마이페이지 조회
    @GetMapping("/mypage")
    public ResponseEntity<?> getMyPageInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = "마이페이지 조회에 성공하였습니다";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg, userService.getMyPageInfo(userDetails.getUser())));
    }

    // 카카오 추가입력, 마이페이지 수정
    @PutMapping("/mypage/profile")
    public ResponseEntity<?> updateInfo(@RequestBody UpdateInfoRequest request,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, userService.updateInfo(request, userDetails.getUser())));
    }

    // 마이페이지 비밀번호 수정
    @PutMapping("/mypage/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid UpdatePasswordRequest request,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, userService.updatePassword(request, userDetails.getUser())));
    }


}