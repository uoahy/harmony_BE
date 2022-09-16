package com.example.harmony.domain.notification.model;

import lombok.Getter;

@Getter
public class NotificationRequest {

    private String domain;

    private String action;

    public NotificationRequest(String domain, String action) {
        this.domain = domain;
        this.action = action;
    }
}
