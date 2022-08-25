package com.example.harmony.domain.community.service;

import com.example.harmony.domain.community.model.Like;
import com.example.harmony.domain.community.model.Post;
import com.example.harmony.domain.community.repository.LikeRepository;
import com.example.harmony.domain.community.repository.PostRepository;
import com.example.harmony.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    LikeRepository likeRepository;

    @Nested
    @DisplayName("게시글 좋아요")
    class doLike {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("존재하지 않는 게시물")
            void postIsEmpty() {
                // given
                User user = User.builder().build();
                boolean like = true;

                LikeService likeService = new LikeService(likeRepository, postRepository);

                when(postRepository.findById(1L))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> likeService.doLike(1L, user, like));

                // then
                assertEquals("404 NOT_FOUND \"게시글이 존재하지 않습니다.\"",exception.getMessage());
            }

        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("좋아요 생성")
            void createLike() {
                // given
                User user = User.builder().build();
                Post post = Post.builder()
                        .id(1L)
                        .build();
                boolean like = true;

                LikeService likeService = new LikeService(likeRepository, postRepository);

                when(postRepository.findById(1L))
                        .thenReturn(Optional.of(post));

                // when
                String result = likeService.doLike(1L,user,like);

                // then
                assertEquals("좋아요를 눌렀습니다.", result);
            }

            @Test
            @DisplayName("좋아요 수정")
            void putLike() {
                // given
                User user = User.builder().build();
                Post post = Post.builder()
                        .id(1L)
                        .build();
                Like savedLike = new Like(true, post, user);
                boolean like = false;

                LikeService likeService = new LikeService(likeRepository, postRepository);

                when(postRepository.findById(1L))
                        .thenReturn(Optional.of(post));

                when(likeRepository.findByPostAndUser(post,user))
                        .thenReturn(Optional.of(savedLike));

                // when
                String result = likeService.doLike(1L,user,like);

                // then
                assertEquals("좋아요를 눌렀습니다.", result);
                assertFalse(savedLike.isLike());
            }

        }
    }

    @Nested
    @DisplayName("게시글 좋아요 취소")
    class  undoLike {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("존재하지 않는 게시물")
            void postIsEmpty() {
                // given
                User user = User.builder().build();

                LikeService likeService = new LikeService(likeRepository, postRepository);

                when(postRepository.findById(1L))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> likeService.undoLike(1L, user));

                // then
                assertEquals("404 NOT_FOUND \"게시글이 존재하지 않습니다.\"",exception.getMessage());
            }

        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void undoLike() {
                // given
                User user = User.builder().build();
                Post post = Post.builder()
                        .id(1L)
                        .build();

                LikeService likeService = new LikeService(likeRepository, postRepository);

                when(postRepository.findById(1L))
                        .thenReturn(Optional.of(post));

                // when
                String result = likeService.undoLike(1L, user);

                // then
                assertEquals("좋아요를 취소하였습니다.", result);
                assertTrue(likeRepository.findByPostAndUser(post, user).isEmpty());

            }
        }
    }

}