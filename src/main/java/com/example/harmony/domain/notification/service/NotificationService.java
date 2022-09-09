package com.example.harmony.domain.notification.service;

import com.example.harmony.domain.notification.model.Notification;
import com.example.harmony.domain.notification.model.NotificationRequest;
import com.example.harmony.domain.notification.model.NotificationResponse;
import com.example.harmony.domain.notification.model.NotificationsListResponse;
import com.example.harmony.domain.notification.repository.NotificationRepository;
import com.example.harmony.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public NotificationsListResponse getNotifications(User user) {
        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        return new NotificationsListResponse(notifications);
    }

    public void createNotification(NotificationRequest notificationRequest, List<User> user) {
        List<Notification> notifications = notificationRepository.saveAll(user.stream()
                .map(x -> new Notification(notificationRequest, x))
                .collect(Collectors.toList()));
        for (Notification notification : notifications) {
            simpMessageSendingOperations.convertAndSend("/topic/user/" + notification.getUser().getNickname(), new NotificationResponse(notification));
        }
    }

    @Transactional
    public void deleteNotifications(User user) {
        notificationRepository.deleteAllByUserId(user.getId());
    }

    public void deleteNotification(Long notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다"));
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "알림 삭제 권한이 없습니다");
        }
        notificationRepository.deleteById(notificationId);
    }
}
