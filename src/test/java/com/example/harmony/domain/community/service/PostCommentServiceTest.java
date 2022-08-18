package com.example.harmony.domain.community.service;

import com.example.harmony.domain.community.entity.Post;
import com.example.harmony.domain.community.entity.PostComment;
import com.example.harmony.domain.community.repository.PostCommentRepository;
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
class PostCommentServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    PostCommentRepository commentRepository;

    @Nested
    @DisplayName("게시글 댓글 작성")
    class createComment {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("존재하지 않는 게시물")
            void postIsEmpty() {
                // given
                User user = User.builder().build();
                Long postId = 1L;
                String content = "댓글 내용";

                PostCommentService commentService = new PostCommentService(commentRepository, postRepository);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> commentService.createComment(postId,content,user));

                // then
                assertEquals("404 NOT_FOUND \"게시물이 존재하지 않습니다.\"",exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void createComment() {
                // given
                User user = User.builder().build();
                Long postId = 1L;
                Post post = Post.builder()
                        .id(postId)
                        .build();
                String content = "댓글 내용";

                PostCommentService commentService = new PostCommentService(commentRepository, postRepository);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.of(post));

                // when
                String result = commentService.createComment(postId,content,user);

                // then
                assertEquals("댓글 작성을 성공하였습니다.", result);
            }
        }
    }

    @Nested
    @DisplayName("게시글 댓글 수정")
    class putComment {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("존재하지 않는 댓글")
            void commentIsEmpty() {
                // given
                User user = User.builder().build();
                Long commentId = 1L;
                String content = "수정할 댓글 내용";

                PostCommentService commentService = new PostCommentService(commentRepository, postRepository);

                when(commentRepository.findById(commentId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> commentService.putComment(commentId,content,user));

                // then
                assertEquals("404 NOT_FOUND \"존재하지 않는 댓글입니다.\"",exception.getMessage());
            }

            @Test
            @DisplayName("수정 권한 없음")
            void noAuthority() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                User writer = User.builder()
                        .id(2L)
                        .build();
                Post post = Post.builder().build();
                Long commentId = 1L;
                String content = "수정할 댓글 내용";
                String savedContent = "원래 댓글 내용";
                PostComment comment = new PostComment(savedContent, post, writer);

                PostCommentService commentService = new PostCommentService(commentRepository, postRepository);

                when(commentRepository.findById(commentId))
                        .thenReturn(Optional.of(comment));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> commentService.putComment(commentId,content,user));

                // then
                assertEquals("403 FORBIDDEN \"댓글에 대한 권한이 없습니다.\"",exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void putComment() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                Long commentId = 1L;
                Post post = Post.builder().build();
                String content = "수정할 댓글 내용";
                String savedContent = "원래 댓글 내용";
                PostComment comment = new PostComment(savedContent, post, user);

                PostCommentService commentService = new PostCommentService(commentRepository, postRepository);

                when(commentRepository.findById(commentId))
                        .thenReturn(Optional.of(comment));

                // when
                String result = commentService.putComment(commentId,content,user);

                // then
                assertEquals("댓글 수정을 성공하였습니다.", result);
                assertEquals(content, comment.getContent());
            }
        }
    }

    @Nested
    @DisplayName("게시글 댓글 삭제")
    class deleteComment {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("존재하지 않는 댓글")
            void commentIsEmpty() {
                // given
                User user = User.builder().build();
                Long commentId = 1L;

                PostCommentService commentService = new PostCommentService(commentRepository, postRepository);

                when(commentRepository.findById(commentId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> commentService.deleteComment(commentId,user));

                // then
                assertEquals("404 NOT_FOUND \"존재하지 않는 댓글입니다.\"",exception.getMessage());
            }

            @Test
            @DisplayName("수정 권한 없음")
            void noAuthority() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                User writer = User.builder()
                        .id(2L)
                        .build();
                Post post = Post.builder().build();
                Long commentId = 1L;
                String content = "댓글 내용";
                PostComment comment = new PostComment(content, post, writer);

                PostCommentService commentService = new PostCommentService(commentRepository, postRepository);

                when(commentRepository.findById(commentId))
                        .thenReturn(Optional.of(comment));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> commentService.deleteComment(commentId,user));

                // then
                assertEquals("403 FORBIDDEN \"댓글에 대한 권한이 없습니다.\"",exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void deleteComment() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                Long commentId = 1L;
                Post post = Post.builder().build();
                String content = "댓글 내용";
                PostComment comment = new PostComment(content, post, user);

                PostCommentService commentService = new PostCommentService(commentRepository, postRepository);

                when(commentRepository.findById(commentId))
                        .thenReturn(Optional.of(comment));

                // when
                String result = commentService.deleteComment(commentId,user);

                // then
                assertEquals("댓글 삭제를 성공하였습니다.", result);
            }
        }
    }
}