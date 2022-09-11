package com.example.harmony.domain.notification.repository;

import com.example.harmony.domain.notification.model.Notification;
import com.example.harmony.domain.user.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    NotificationRepository notificationRepository;

    @Test
    @DisplayName("알림 최근순 조회")
    void findAllByUserIdOrderByCreatedAtDesc() {
        // given
        User user1 = User.builder()
                .email("email1")
                .name("name1")
                .build();

        User user2 = User.builder()
                .email("email2")
                .name("name2")
                .build();

        Notification notification1 = Notification.builder()
                .user(user1)
                .build();

        Notification notification2 = Notification.builder()
                .user(user1)
                .build();

        Notification notification3 = Notification.builder()
                .user(user2)
                .build();

        Notification notification4 = Notification.builder()
                .user(user2)
                .build();

        em.persist(user1);
        em.persist(user2);
        em.persist(notification1);
        em.persist(notification2);
        em.persist(notification3);
        em.persist(notification4);

        // when
        List<Notification> notifications = notificationRepository.findAllByUserIdOrderByCreatedAtDesc(user1.getId());

        // then
        assertEquals(Arrays.asList(notification2, notification1), notifications);
    }
}