package com.example.harmony.domain.community.service;

import com.example.harmony.domain.community.dto.PostCommentResponse;
import com.example.harmony.domain.community.dto.PostRequest;
import com.example.harmony.domain.community.dto.PostResponse;
import com.example.harmony.domain.community.model.Like;
import com.example.harmony.domain.community.model.Post;
import com.example.harmony.domain.community.model.PostComment;
import com.example.harmony.domain.community.model.Tag;
import com.example.harmony.domain.community.repository.LikeRepository;
import com.example.harmony.domain.community.repository.PostCommentRepository;
import com.example.harmony.domain.community.repository.PostRepository;
import com.example.harmony.domain.community.repository.TagRepository;
import com.example.harmony.domain.user.entity.Family;
import com.example.harmony.domain.user.entity.User;
import com.example.harmony.global.s3.S3Config;
import com.example.harmony.global.s3.S3Service;
import com.example.harmony.global.s3.UploadResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {S3Config.class, S3Service.class})
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    TagRepository tagRepository;

    @Mock
    LikeRepository likeRepository;

    @Mock
    PostCommentRepository postCommentRepository;

    @Mock
    S3Service s3Service;

    @Nested
    @DisplayName("커뮤니티 게시글 작성")
    class CreatePost {

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("이미지 미첨부")
            void withoutImage() {
                // given
                User user = User.builder().build();
                String title = "게시글 제목";
                String category = "아빠";
                String content = "이것은 게시글의 내용입니다.";
                List<String> tags = new ArrayList<>();
                tags.add("이것은");
                tags.add("태그");
                tags.add("입니다");

                PostRequest postRequest = new PostRequest(title, category, content, tags);

                PostService postService = new PostService(postRepository,postCommentRepository, tagRepository, likeRepository, s3Service);

                // when
                String result = postService.createPost(null, postRequest, user);

                // then
                assertEquals("게시글 작성이 완료되었습니다.", result);
            }

            @Test
            @DisplayName("이미지 첨부")
            void withImage() {
                // given
                User user = User.builder().build();
                String title = "게시글 제목";
                String category = "아빠";
                String content = "이것은 게시글의 내용입니다.";
                List<String> tags = new ArrayList<>();
                tags.add("이것은");
                tags.add("태그");
                tags.add("입니다");
                MockMultipartFile image = new MockMultipartFile("imageFiles", (byte[]) null);

                PostRequest postRequest = new PostRequest(title, category, content, tags);

                PostService postService = new PostService(postRepository,postCommentRepository, tagRepository, likeRepository, s3Service);

                when(s3Service.uploadFile(image))
                        .thenReturn(new UploadResponse("imageUrl", "filename"));

                // when
                String result = postService.createPost(image, postRequest, user);

                // then
                assertEquals("게시글 작성이 완료되었습니다.", result);
            }
        }
    }

    @Nested
    @DisplayName("커뮤니티 게시글 조회")
    class getPost {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("존재하지 않는 게시글")
            void postIsEmpty() {
                // given
                User user = User.builder().build();
                Long postId = 1L;

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> postService.getPost(postId,user));

                // then
                assertEquals("404 NOT_FOUND \"게시물이 존재하지 않습니다.\"",exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("정상 케이스")
            void isPoster() {
                // given
                // 게시글 작성자, 유저: 레벨 3, 꽃x
                Family family1 = Family.builder()
                        .flower(false)
                        .totalScore(770)
                        .build();
                User user1 = User.builder()
                        .id(1L)
                        .family(family1)
                        .nickname("짱구")
                        .build();

                // 댓글 작성자: 레벨0, 꽃 o
                Family family2 = Family.builder()
                        .flower(true)
                        .totalScore(33)
                        .build();
                User user2 = User.builder()
                        .id(2L)
                        .family(family2)
                        .nickname("짱아")
                        .build();

                // 태그
                List<Tag> tagList = new ArrayList<>();
                Tag tag1 = Tag.builder()
                        .tag("이것은")
                        .build();
                Tag tag2 = Tag.builder()
                        .tag("태그")
                        .build();
                tagList.add(tag1);
                tagList.add(tag2);

                // 좋아요
                List<Like> likeList = new ArrayList<>();
                Like like1 = Like.builder()
                        .like(true)
                        .user(user1)
                        .build();
                likeList.add(like1);

                // 댓글
                List<PostComment> commentList = new ArrayList<>();
                PostComment comment1 = PostComment.builder()
                        .content("게시글 작성자의 댓글")
                        .user(user1)
                        .build();
                PostComment comment2 = PostComment.builder()
                        .content("댓글 작성자의 댓글")
                        .user(user2)
                        .build();
                commentList.add(comment1);
                commentList.add(comment2);

                // 게시글
                String title = "제목";
                String category = "아빠";
                String content = "이것은 게시글 내용입니다.";
                Post post = Post.builder()
                        .id(1L)
                        .title(title)
                        .category(category)
                        .content(content)
                        .tags(tagList)
                        .likes(likeList)
                        .user(user1)
                        .comments(commentList)
                        .build();

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(1L))
                        .thenReturn(Optional.of(post));

                when(likeRepository.findByPostAndUser(post, user1))
                        .thenReturn(Optional.of(like1));

                when(postCommentRepository.findAllByPostContainingOrderByCreatedAtDesc(post))
                        .thenReturn(commentList);

                //when
                PostResponse postResponse = postService.getPost(1L, user1);
                List<PostCommentResponse> postCommentList = postResponse.getComments();

                // then
                assertEquals(title, postResponse.getTitle());
                assertEquals(content, postResponse.getContent());
                assertEquals("이것은", postResponse.getTags().get(0));
                assertNull(postResponse.getImageUrl());
                assertEquals("짱구",postCommentList.get(0).getCommenter().get("nickname"));
                assertEquals(3,postCommentList.get(0).getCommenter().get("level"));
                assertEquals(1, postResponse.getLikeCount());
                assertTrue(postResponse.isLike());
            }
        }

    }

    @Nested
    @DisplayName("커뮤니티 게시글 목록 조회")
    class getPosts {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("유효하지 않은 카테고리")
            void invalidCategory() {
                // given
                String category = "반려동물";
                int page = 0;
                int size = 9;

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> postService.getPosts(category, page, size));

                // then
                assertEquals("400 BAD_REQUEST \"유효하지 않은 카테고리입니다.\"",exception.getMessage());
            }
        }
    }

    @Nested
    @DisplayName("커뮤니티 게시글 수정")
    class editPost {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("존재하지 않는 게시물")
            void postIsEmpty() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                Long postId = 1L;

                String title = "게시글 제목";
                String category = "아빠";
                String content = "이것은 게시글의 내용입니다.";
                List<String> tags = new ArrayList<>();
                tags.add("이것은");
                tags.add("태그");

                PostRequest postRequest = new PostRequest(title, category, content, tags);

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> postService.putPost(postId, null, postRequest, user));

                // then
                assertEquals("404 NOT_FOUND \"게시물이 존재하지 않습니다.\"",exception.getMessage());
            }

            @Test
            @DisplayName("수정권한 없음")
            void noAuthority() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                User writer = User.builder()
                        .id(2L)
                        .build();
                Long postId = 1L;
                Post post = Post.builder()
                        .user(writer)
                        .id(postId)
                        .build();

                String title = "게시글 제목";
                String category = "아빠";
                String content = "이것은 게시글의 내용입니다.";
                List<String> tags = new ArrayList<>();
                tags.add("이것은");
                tags.add("태그");

                PostRequest postRequest = new PostRequest(title, category, content, tags);

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.of(post));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> postService.putPost(postId, null, postRequest, user));

                // then
                assertEquals("403 FORBIDDEN \"게시글에 대한 권한이 없습니다.\"",exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("이미지 미첨부 → 미첨부")
            void withoutToWithout() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                Long postId = 1L;

                Post post = Post.builder()
                        .user(user)
                        .title("원래 게시글")
                        .content("이것은 원래 게시글의 내용입니다.")
                        .id(postId)
                        .build();

                String title = "게시글 제목";
                String category = "아빠";
                String content = "이것은 수정한 게시글의 내용입니다.";
                List<String> tags = new ArrayList<>();
                tags.add("이것은");
                tags.add("태그");
                tags.add("입니다");

                PostRequest postRequest = new PostRequest(title, category, content, tags);

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.of(post));

                // when
                String result = postService.putPost(postId, null, postRequest, user);

                // then
                assertEquals("게시글 수정이 완료되었습니다.", result);
                assertEquals(title, post.getTitle());
                assertEquals(category,post.getCategory());
                assertEquals(content, post.getContent());
                assertNull(post.getImageUrl());
                assertNull(post.getImageFilename());

            }

            @Test
            @DisplayName("이미지 미첨부 → 첨부")
            void withoutToWith() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                Long postId = 1L;

                Post post = Post.builder()
                        .user(user)
                        .title("원래 게시글")
                        .content("이것은 원래 게시글의 내용입니다.")
                        .id(postId)
                        .build();

                String title = "게시글 제목";
                String category = "아빠";
                String content = "이것은 수정한 게시글의 내용입니다.";
                List<String> tags = new ArrayList<>();
                tags.add("이것은");
                tags.add("태그");
                tags.add("입니다");
                MockMultipartFile image = new MockMultipartFile("imageFile", (byte[]) null);

                PostRequest postRequest = new PostRequest(title, category, content, tags);

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.of(post));

                when(s3Service.uploadFile(image))
                        .thenReturn(new UploadResponse("imageUrl", "filename"));

                // when
                String result = postService.putPost(postId, image, postRequest, user);

                // then
                assertEquals("게시글 수정이 완료되었습니다.", result);
                assertEquals(title, post.getTitle());
                assertEquals(category,post.getCategory());
                assertEquals(content, post.getContent());
                assertNotNull(post.getImageUrl());
                assertNotNull(post.getImageFilename());
            }
        }
    }

    @Nested
    @DisplayName("커뮤니티 게시글 삭제")
    class deletePost {

        @Nested
        @DisplayName("실패")
        class Fails {

            @Test
            @DisplayName("존재하지 않는 게시물")
            void postIsEmpty() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                Long postId = 1L;

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.empty());

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> postService.deletePost(postId, user));

                // then
                assertEquals("404 NOT_FOUND \"게시물이 존재하지 않습니다.\"",exception.getMessage());
            }

            @Test
            @DisplayName("삭제 권한 없음")
            void noAuthority() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                User writer = User.builder()
                        .id(2L)
                        .build();
                Long postId = 1L;
                Post post = Post.builder()
                        .user(writer)
                        .id(postId)
                        .build();

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.of(post));

                // when
                Exception exception = assertThrows(ResponseStatusException.class, () -> postService.deletePost(postId, user));

                // then
                assertEquals("403 FORBIDDEN \"게시글에 대한 권한이 없습니다.\"",exception.getMessage());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @DisplayName("이미지 미첨부")
            void withoutImage() {
                // given
                User user = User.builder()
                        .id(1L)
                        .build();
                Long postId = 1L;
                Post post = Post.builder()
                        .user(user)
                        .id(postId)
                        .build();

                PostService postService = new PostService(postRepository, postCommentRepository, tagRepository, likeRepository, s3Service);

                when(postRepository.findById(postId))
                        .thenReturn(Optional.of(post));

                // when
                String result = postService.deletePost(postId, user);

                // then
                assertEquals("게시글 삭제가 완료되었습니다.", result);
            }
        }
    }

}
