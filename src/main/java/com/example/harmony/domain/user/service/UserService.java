package com.example.harmony.domain.user.service;

import com.example.harmony.domain.community.repository.LikeRepository;
import com.example.harmony.domain.community.repository.PostCommentRepository;
import com.example.harmony.domain.community.repository.PostRepository;
import com.example.harmony.domain.user.dto.*;
import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.model.Feedback;
import com.example.harmony.domain.user.model.RoleEnum;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.user.repository.FamilyRepository;
import com.example.harmony.domain.user.repository.FeedbackRepository;
import com.example.harmony.domain.user.repository.UserRepository;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final FamilyRepository familyRepository;
    private final PasswordEncoder passwordEncoder;
    private final FeedbackRepository feedbackRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository commentRepository;
    private final LikeRepository likeRepository;

    // 회원가입
    @Transactional
    public String signup(SignupRequest requestDto) {
        String email = requestDto.getEmail();
        String nickname = requestDto.getNickname();
        String password = requestDto.getPassword();

        // 이메일 중복체크
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }
        // 닉네임 중복체크
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다.");
        }
        // 비밀번호 일치 여부
        if (!password.equals(requestDto.getPasswordConfirm())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(email, requestDto.getName(), nickname, encodedPassword, requestDto.getGender()));
        return "회원가입을 성공하였습니다.";

    }

    // 이메일 중복체크
    @Transactional
    public CheckResponse emailChk(String email) {
        // 빈 값 금지
        if (email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일을 입력해주세요.");
        }
        // 이메일 형식
        String regex = "^[a-zA-Z\\d+-_.]+@[a-zA-Z\\d-]+\\.[a-zA-Z\\d-.]+$";
        if (!Pattern.matches(regex, email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 형식이 아닙니다.");
        }
        // 이메일 중복체크
        if (userRepository.findByEmail(email).isPresent()) {
            return new CheckResponse(false);
        }
        return new CheckResponse(true);
    }

    // 닉네임 중복체크
    @Transactional
    public CheckResponse nicknameChk(String nickname) {
        // 빈 값 금지
        if (nickname.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임을 입력해주세요.");
        }
        // 길이 2~20자
        if (nickname.length() < 2 || nickname.length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "닉네임은 2~20자 내로 입력해야합니다.");
        }
        // 닉네임 중복체크
        if (userRepository.findByNickname(nickname).isPresent()) {
            return new CheckResponse(false);
        }
        return new CheckResponse(true);

    }

    // 가족코드 입력
    public String enterFamilyCode(String familyCode, UserDetailsImpl userDetails) {
        Family family = familyRepository.findByFamilyCode(familyCode).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효한 가족코드가 아닙니다.")
        );

        User user = userDetails.getUser();
        user.setFamily(family);
        userRepository.save(user);
        return "가족 연결이 완료되었습니다.";
    }

    // 역할 설정
    public String setRole(String role, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        RoleEnum roleEnum = RoleEnum.nameOf(role);
        user.setRole(roleEnum);
        userRepository.save(user);
        return "역할 설정을 완료하였습니다.";
    }

    // 카카오유저 및 가족, 역할, 추가정보 설정여부
    public Map<String, Object> getUserInfo(User user) {
        Map<String, Object> result = new HashMap<>();

        boolean isFamily;
        isFamily = user.getFamily() != null;
        result.put("isFamily", isFamily);

        boolean hasRole;
        hasRole = user.getRole() != null;
        result.put("hasRole", hasRole);

        boolean kakaoUser;
        kakaoUser = user.getPassword() == null;
        result.put("kakaoUser", kakaoUser);

        boolean hasAllInfo;
        hasAllInfo = user.getNickname() != null;
        result.put("hasAllInfo", hasAllInfo);

        return result;
    }

    // 회원탈퇴
    @Transactional
    public String withdrawal(String password, User user) {
        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        // 커뮤니티 게시글, 좋아요, 댓글 삭제
        if(postRepository.existsByUser(user)) {
            postRepository.deleteAllByUser(user);
        }

        if(commentRepository.existsByUser(user)) {
            commentRepository.deleteAllByUser(user);
        }

        if(likeRepository.existsByUser(user)) {
            likeRepository.deleteAllByUser(user);
        }

        userRepository.delete(user);

        return "회원탈퇴가 완료되었습니다.";

    }

    // 탈퇴고객 피드백
    public String getFeedback(String feedback) {
        // 빈 값 금지
        if (feedback.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "피드백을 작성해주세요.");
        }

        // 길이 15자 이상
        if (feedback.length() < 15) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "피드백을 15자 이상 입력해주세요.");
        }

        feedbackRepository.save(new Feedback(feedback));

        return "피드백을 성공적으로 받았습니다.";
    }

    // 마이페이지 조회
    public MyPageResponse getMyPageInfo(User user) {
        return new MyPageResponse(user);
    }

    // 카카오 추가입력, 마이페이지 수정
    public String updateInfo(UpdateInfoRequest request, User user) {
        // 업데이트 용도 확인
        String useFor = request.getUpdateFor();
        if (useFor.equals("kakao")) {
            String gender = request.getGender();
            String nickname = request.getNickname();

            // 유효성검사
            nicknameChk(nickname);
            if (gender.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "성별을 선택해주세요.");
            }

            user.updateKakao(request);
            userRepository.save(user);
        } else if (useFor.equals("mypage")) {
            String nickname = request.getNickname();
            nicknameChk(nickname);

            user.updateMyPage(nickname);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "설정한 용도를 확인해주십시오.");
        }

        return "정보 수정이 완료되었습니다.";
    }

    // 마이페이지 비밀번호 수정
    public String updatePassword(UpdatePasswordRequest request, User user) {
        // 기존 비밀번호 확인
        String existingPassword = request.getExistingPassword();
        if (!passwordEncoder.matches(existingPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀번호를 정확하게 입력해주세요.");
        }

        // 비밀번호 일치 여부
        String password = request.getPassword();
        if (!password.equals(request.getPasswordConfirm())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        user.updatePassword(encodedPassword);
        userRepository.save(user);

        return "비밀번호 수정이 완료되었습니다.";
    }
}
