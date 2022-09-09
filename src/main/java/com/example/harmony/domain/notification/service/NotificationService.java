package com.example.harmony.domain.notification.service;

import com.example.harmony.domain.notification.model.Notification;
import com.example.harmony.domain.notification.model.NotificationsListResponse;
import com.example.harmony.domain.notification.repository.NotificationRepository;
import com.example.harmony.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationsListResponse getNotifications(User user) {
        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        return new NotificationsListResponse(notifications);
    }
}
