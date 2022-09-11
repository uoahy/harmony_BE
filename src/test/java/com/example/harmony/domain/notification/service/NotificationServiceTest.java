package com.example.harmony.domain.notification.service;

import com.example.harmony.domain.notification.model.Notification;
import com.example.harmony.domain.notification.repository.NotificationRepository;
import com.example.harmony.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    NotificationRepository notificationRepository;

    @Nested
    @DisplayName("알림 삭제")
    class DeleteNotification {

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @DisplayName("존재하지않는 알림")
            void notification_not_found() {
                // given
                Long notificationId = -1L;

                User user = User.builder().build();

                when(notificationRepository.findById(notificationId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> notificationService.deleteNotification(notificationId, user));

                // then
                assertEquals("404 NOT_FOUND \"존재하지않는 알림입니다\"", exception.getMessage());
            }

            @Test
            @DisplayName("다른 유저가 알림 삭제 시도")
            void not_notification_user() {
                // given
                User user1 = User.builder()
                        .id(1L)
                        .build();

                User user2 = User.builder()
                        .id(2L)
                        .build();

                Long notificationId = 1L;
                Notification notification = Notification.builder()
                        .user(user1)
                        .build();

                when(notificationRepository.findById(notificationId))
                        .thenReturn(Optional.of(notification));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> notificationService.deleteNotification(notificationId, user2));

                // then
                assertEquals("403 FORBIDDEN \"알림 삭제 권한이 없습니다\"", exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void success() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();

                Long notificationId = 1L;
                Notification notification = Notification.builder()
                        .user(user)
                        .build();

                when(notificationRepository.findById(notificationId))
                        .thenReturn(Optional.of(notification));

                // when & then
                assertDoesNotThrow(() -> notificationService.deleteNotification(notificationId, user));
            }
        }
    }
}