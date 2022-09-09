package com.example.harmony.domain.notification.controller;

import com.example.harmony.domain.notification.model.NotificationsListResponse;
import com.example.harmony.domain.notification.service.NotificationService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<SuccessResponse> getNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        NotificationsListResponse notificationsListResponse = notificationService.getNotifications(userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "알림 조회 성공", notificationsListResponse), HttpStatus.OK);
    }

    @DeleteMapping("/notifications")
    public ResponseEntity<SuccessResponse> deleteNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        notificationService.deleteNotifications(userDetails.getUser());
        return new ResponseEntity<>(new SuccessResponse(HttpStatus.OK, "알림 전체 삭제 성공"), HttpStatus.OK);
    }
}
