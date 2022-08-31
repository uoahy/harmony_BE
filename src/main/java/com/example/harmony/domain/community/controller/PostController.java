package com.example.harmony.domain.community.controller;

import com.example.harmony.domain.community.dto.PostRequest;
import com.example.harmony.domain.community.service.PostService;
import com.example.harmony.global.common.SuccessResponse;
import com.example.harmony.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    // 게시글 작성
    @PostMapping(value = "/api/posts", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createPost(
            @RequestPart(required = false) MultipartFile image,
            @RequestPart @Valid PostRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = postService.createPost(image, request, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK,msg));
    }

    // 게시글 조회
    @GetMapping("/api/posts/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String msg= "게시글 조회를 성공하였습니다.";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg, postService.getPost(postId,userDetails.getUser())));
    }

    // 게시글 목록 조회
    @GetMapping("/api/posts")
    public ResponseEntity<?> getPosts(@RequestParam String category,
                                      @RequestParam int page,
                                      @RequestParam int size) {
        String msg = "게시글 목록 조회를 성공하였습니다";
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg, postService.getPosts(category, page, size)));
    }

    // 게시글 수정
    @PutMapping(value ="/api/posts/{postId}" ,consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> putPost(@PathVariable Long postId,
                                     @RequestPart(required = false) MultipartFile image,
                                     @RequestPart @Valid PostRequest request,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = postService.putPost(postId, image, request, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg));
    }

    // 게시글 삭제
    @DeleteMapping("/api/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String msg = postService.deletePost(postId, userDetails.getUser());
        return ResponseEntity.ok(new SuccessResponse<>(HttpStatus.OK, msg));
    }
}
