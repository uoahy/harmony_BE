package com.example.harmony.domain.user.service;

import com.example.harmony.domain.user.dto.FamilyInfoResponse;
import com.example.harmony.domain.user.model.Family;
import com.example.harmony.domain.user.model.RoleEnum;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.domain.user.repository.FamilyRepository;
import com.example.harmony.domain.user.repository.UserRepository;
import com.example.harmony.global.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FamilyServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    FamilyRepository familyRepository;

    @Nested
    @DisplayName("가족코드 생성")
    class createFamily {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("가족이름이 존재하는 경우")
            void familyNameIsExist() {
                // given
                User user = User.builder()
                        .build();
                UserDetailsImpl userDetails = new UserDetailsImpl(user);
                String familyName = "우당탕탕 순이네";

                FamilyService familyService = new FamilyService(familyRepository, userRepository);

                when(familyRepository.findByFamilyName(familyName))
                        .thenReturn(Optional.of(Family.builder().build()));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> familyService.createFamily(familyName, userDetails));

                // then
                assertEquals("400 BAD_REQUEST \"이미 존재하는 가족이름입니다.\"", exception.getMessage());
            }

            @Test
            @DisplayName("이미 가족이 등록되어 있는 경우")
            void hasFamily() {
                // given
                User user = User.builder()
                        .family(Family.builder().build())
                        .build();
                UserDetailsImpl userDetails = new UserDetailsImpl(user);
                String familyName = "우당탕탕 순이네";

                FamilyService familyService = new FamilyService(familyRepository, userRepository);

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> familyService.createFamily(familyName, userDetails));

                // then
                assertEquals("400 BAD_REQUEST \"이미 가족이 등록된 유저입니다.\"", exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                User user = User.builder().build();
                UserDetailsImpl userDetails = new UserDetailsImpl(user);
                String familyName = "우당탕탕 순이네";

                FamilyService familyService = new FamilyService(familyRepository, userRepository);

                // when
                Map<String, String> result = familyService.createFamily(familyName, userDetails);

                // then
                assertEquals(user.getFamily().getFamilyCode(), result.get("familyCode"));
            }
        }
    }

    @Nested
    @DisplayName("가족 정보 조회")
    class getFamily {

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                String familyName = "우당탕탕 순이네";
                List<User> members = new ArrayList<>();
                Family family = Family.builder()
                        .familyName(familyName)
                        .members(members)
                        .build();

                User user = User.builder()
                        .id(1L)
                        .name("홍길동")
                        .role(RoleEnum.FATHER)
                        .family(family)
                        .build();

                User user1 = User.builder()
                        .id(2L)
                        .name("이영희")
                        .role(RoleEnum.MOTHER)
                        .family(family)
                        .build();

                members.add(user);
                members.add(user1);

                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                FamilyService familyService = new FamilyService(familyRepository, userRepository);

                // when
                FamilyInfoResponse result = familyService.getFamily(userDetails);

                // then
                assertEquals(familyName, result.getFamilyName());
                assertEquals("홍길동", result.getMembers().get(0).getName());
                assertEquals("이영희", result.getMembers().get(1).getName());
            }

            @Test
            @DisplayName("멤버 중 역할 미설정자")
            void successWithNoRole() {
                // given
                String familyName = "우당탕탕 순이네";
                List<User> members = new ArrayList<>();
                Family family = Family.builder()
                        .familyName(familyName)
                        .members(members)
                        .build();

                User user = User.builder()
                        .id(1L)
                        .name("홍길동")
                        .family(family)
                        .build();

                User user1 = User.builder()
                        .id(2L)
                        .name("이영희")
                        .role(RoleEnum.MOTHER)
                        .family(family)
                        .build();

                members.add(user);
                members.add(user1);

                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                FamilyService familyService = new FamilyService(familyRepository, userRepository);

                // when
                FamilyInfoResponse result = familyService.getFamily(userDetails);

                // then
                assertEquals(familyName, result.getFamilyName());
                assertEquals(RoleEnum.NOBODY.getRole(), result.getMembers().get(0).getRole());
                assertEquals(RoleEnum.MOTHER.getRole(), result.getMembers().get(1).getRole());
            }
        }
    }

    @Nested
    @DisplayName("가족코드 조회")
    class getFamilyCode {

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                String familyCode = "sd53GHD4f%";

                Family family = Family.builder()
                        .familyCode(familyCode)
                        .build();

                User user = User.builder()
                        .family(family)
                        .build();

                FamilyService familyService = new FamilyService(familyRepository, userRepository);

                // when
                Map<String, String> result = familyService.getFamilyCode(user);

                // then
                assertEquals(familyCode, result.get("familyCode"));
            }
        }
    }


}
