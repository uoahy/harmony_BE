package com.example.harmony.domain.community.service;

import com.example.harmony.domain.community.entity.Post;
import com.example.harmony.domain.community.entity.PostComment;
import com.example.harmony.domain.community.repository.PostCommentRepository;
import com.example.harmony.domain.community.repository.PostRepository;
import com.example.harmony.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

    // 게시글 댓글 작성
    public void createComment(Long postId, String content, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"게시물이 존재하지 않습니다.")
        );

        PostComment postComment = new PostComment(content, post, user);
        postCommentRepository.save(postComment);
    }

    // 게시글 댓글 수정
    public void putComment(Long commentId, String content, User user) {
        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 댓글입니다.")
        );

        // 댓글 작성자 여부
        User writer = postComment.getUser();
        getAuthority(writer,user);

        postComment.putComment(content);
        postCommentRepository.save(postComment);
    }

    // 게시글 댓글 삭제
    public void deleteComment(Long commentId, User user) {
        PostComment postComment = postCommentRepository.findById(commentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"존재하지 않는 댓글입니다.")
        );

        // 댓글 작성자 여부
        User writer = postComment.getUser();
        getAuthority(writer, user);

        postCommentRepository.deleteById(commentId);
    }

    // 작성자 일치 여부
    public void getAuthority(User writer, User user) {
        if(!writer.getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "댓글에 대한 권한이 없습니다.");
        }
    }


}
