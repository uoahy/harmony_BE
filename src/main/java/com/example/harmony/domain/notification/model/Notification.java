package com.example.harmony.domain.notification.model;

import com.example.harmony.domain.user.model.User;
import com.example.harmony.global.common.TimeStamp;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Notification extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String domain;

    private String action;

    @ManyToOne
    private User user;

    public Notification(NotificationRequest notificationRequest, User user) {
        this.domain = notificationRequest.getDomain();
        this.action = notificationRequest.getAction();
        this.user = user;
    }
}
