package com.example.harmony.domain.community.controller;

import com.example.harmony.domain.community.service.PostCommentService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class PostCommentController {

    private final PostCommentService postCommentService;

    // 게시글 댓글 작성
    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long postId,
                                           @RequestBody Map<String, String> map,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg= "댓글 작성을 성공하였습니다";
        postCommentService.createComment(postId, map.get("content"), userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg));
    }

    // 게시글 댓글 수정
    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> putComment(@PathVariable Long commentId,
                                        @RequestBody Map<String, String> map,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = "댓글 수정을 성공하였습니다.";
        postCommentService.putComment(commentId, map.get("content"), userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg));
    }

    // 게시글 댓글 삭제
    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = "댓글 삭제를 성공하였습니다.";
        postCommentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg));
    }
}
