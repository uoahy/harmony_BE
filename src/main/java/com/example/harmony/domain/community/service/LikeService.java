package com.example.harmony.domain.community.service;

import com.example.harmony.domain.community.entity.Like;
import com.example.harmony.domain.community.entity.Post;
import com.example.harmony.domain.community.repository.LikeRepository;
import com.example.harmony.domain.community.repository.PostRepository;
import com.example.harmony.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    // 게시글 좋아요 + 이미 누른 사람
    public void doLike(Long postId, User user,boolean like) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"게시글이 존재하지 않습니다.")
        );

        // 이미 누른 사람
        if(likeRepository.findByPostAndUser(post, user).isPresent()) {
            Like isPresent = likeRepository.findByPostAndUser(post,user).orElseThrow(
                    ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"사용자가 이 게시글에 좋아요를 누르지 않았습니다.")
            );
            isPresent.putLike(like);
            likeRepository.save(isPresent);
        }
        likeRepository.save(new Like(like, post, user));
    }

    // 게시글 좋아요 취소
    public void undoLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,"게시글이 존재하지 않습니다.")
        );
        likeRepository.deleteLikeByPostAndUser(post, user);
    }
}
