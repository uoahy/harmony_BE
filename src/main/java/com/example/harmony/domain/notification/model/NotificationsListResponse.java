package com.example.harmony.domain.notification.model;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class NotificationsListResponse {

    List<NotificationResponse> notifications;

    public NotificationsListResponse(List<Notification> notifications) {
        this.notifications = notifications.stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
    }
}
