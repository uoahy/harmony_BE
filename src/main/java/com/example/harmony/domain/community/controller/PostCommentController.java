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
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> createComment(@PathVariable Long postId,
                                           @RequestBody Map<String, String> map,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = postCommentService.createComment(postId, map.get("content"), userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg));
    }

    // 게시글 댓글 수정
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> putComment(@PathVariable Long commentId,
                                        @RequestBody Map<String, String> map,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = postCommentService.putComment(commentId, map.get("content"), userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg));
    }

    // 게시글 댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = postCommentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg));
    }
}
