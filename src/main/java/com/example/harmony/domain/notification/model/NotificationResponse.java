package com.example.harmony.domain.notification.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {

    private Long id;

    private String domain;

    private String action;

    private LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.domain = notification.getDomain();
        this.action = notification.getAction();
        this.createdAt = notification.getCreatedAt();
    }
}
